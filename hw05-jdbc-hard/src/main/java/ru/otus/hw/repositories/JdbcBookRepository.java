package ru.otus.hw.repositories;

import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.*;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import ru.otus.hw.exceptions.EntityNotFoundException;
import ru.otus.hw.models.Author;
import ru.otus.hw.models.Book;
import ru.otus.hw.models.Genre;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
public class JdbcBookRepository implements BookRepository {
    private final NamedParameterJdbcTemplate jdbc;

    private final GenreRepository genreRepository;

    @Override
    public Optional<Book> findById(long id) {
        Map<String, Object> param = Collections.singletonMap("id", id);
        Book book = jdbc.query("select b.id as book_id, b.title as title, " +
                "                          a.id as author_id, a.full_name as author_full_name, " +
                "                          g.id as genre_id, g.name as genre_name " +
                "from books b " +
                "left join authors as a on b.author_id = a.id " +
                "left join books_genres as bg on b.id = bg.book_id " +
                "left join genres as g on bg.genre_id = g.id " +
                "where b.id = :id", param, new BookResultSetExtractor());

        return Optional.ofNullable(book);
    }

    @Override
    public List<Book> findAll() {
        List<Genre> genres = genreRepository.findAll();
        List<BookGenreRelation> bookGenreRelations = getAllGenreRelations();
        List<Book> books = getAllBooksWithoutGenres();

        mergeBooksInfo(books, genres, bookGenreRelations);

        return books;
    }

    @Override
    public Book save(Book book) {
        if(book.getId() == 0) {
            return insert(book);
        }
        return update(book);
    }

    @Override
    public void deleteById(long id) {
        Map<String, Object> params = Collections.singletonMap("book_id", id);
        jdbc.update("delete from books where id = :book_id", params);
    }

    private void mergeBooksInfo(List<Book> booksWithoutGenres, List<Genre> genres,
                                List<BookGenreRelation> relations) {
        Map<Long, List<BookGenreRelation>> mappedRelations = relations.stream()
                .collect(Collectors.groupingBy(BookGenreRelation::bookId));
        mappedRelations.forEach((k,v) -> {
            Optional<Book> book = booksWithoutGenres.stream()
                    .filter(b -> b.getId() == k)
                    .findFirst();
            if (book.isPresent()) {
                List<Genre> bookGenres =
                        genres.stream().filter(g -> v.stream().anyMatch(r -> r.genreId == g.getId()))
                                .toList();
                book.get().setGenres(bookGenres);
            }
        });
    }

    private List<Book> getAllBooksWithoutGenres() {
        return jdbc.query("select b.id as book_id, b.title as title, " +
                "                     a.id as author_id, a.full_name as author_full_name " +
                "from books as b " +
                "left join authors as a on b.author_id = a.id", new BookRowMapper());
    }

    private List<BookGenreRelation> getAllGenreRelations() {
        return jdbc.query("select book_id, genre_id from books_genres", new BookGenreRowMapper());
    }

    private Book insert(Book book) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        SqlParameterSource sqlParams = new MapSqlParameterSource()
                .addValue("title",  book.getTitle())
                .addValue("author_id", book.getAuthor().getId());
        jdbc.update("insert into books(title, author_id) values(:title, :author_id)",
                sqlParams, keyHolder);
        book.setId(keyHolder.getKeyAs(Long.class));
        batchInsertGenresRelationsFor(book);
        return book;
    }

    private Book update(Book book) {
        SqlParameterSource sqlParams = new MapSqlParameterSource()
                .addValue("book_id", book.getId())
                .addValue("title",  book.getTitle())
                .addValue("author_id", book.getAuthor().getId());

        int updatedEntities = jdbc.update("update books " +
                "set title = :title, author_id = :author_id " +
                "where id = :book_id", sqlParams);
        if(updatedEntities == 0) {
            throw new EntityNotFoundException("Book with id %d not found".formatted(book.getId()));
        }

        removeGenresRelationsFor(book);
        batchInsertGenresRelationsFor(book);
        return book;
    }

    private void batchInsertGenresRelationsFor(Book book) {
        List<BookGenreRelation> bookGenreRelations = new ArrayList<>();
        for(Genre genre : book.getGenres()) {
            bookGenreRelations.add(new BookGenreRelation(book.getId(), genre.getId()));
        }
        SqlParameterSource[] sqlParams = SqlParameterSourceUtils.createBatch(bookGenreRelations);

        jdbc.batchUpdate("insert into books_genres(book_id, genre_id) values(:bookId, :genreId)", sqlParams);
    }

    private void removeGenresRelationsFor(Book book) {
        Map<String, Object> mapBookId = Collections.singletonMap("book_id", book.getId());
        jdbc.update("delete from books_genres where book_id = :book_id", mapBookId);
    }

    private static class BookRowMapper implements RowMapper<Book> {

        @Override
        public Book mapRow(ResultSet rs, int rowNum) throws SQLException {
            long id = rs.getLong("book_id");
            String title = rs.getString("title");
            long authorId = rs.getLong("author_id");
            String authorFullName = rs.getString("author_full_name");

            Book book = new Book();
            book.setId(id);
            book.setTitle(title);
            book.setAuthor(new Author(authorId, authorFullName));
            return book;
        }
    }

    private static class BookGenreRowMapper implements RowMapper<BookGenreRelation> {

        @Override
        public BookGenreRelation mapRow(ResultSet rs, int rowNum) throws SQLException {
            long bookId = rs.getLong("book_id");
            long genreId = rs.getLong("genre_id");
            return new BookGenreRelation(bookId, genreId);
        }
    }

    private static class BookResultSetExtractor implements ResultSetExtractor<Book> {

        @Override
        public Book extractData(ResultSet rs) throws SQLException, DataAccessException {
            Map<Long, Book> books = new HashMap<>();
            List<Genre> bookGenres = new ArrayList<>();
            while (rs.next()) {
                long bookId = rs.getLong("book_id");
                Book book = books.get(bookId);
                if (book == null) {
                    book = new Book();
                    bookGenres = new ArrayList<>();

                    String title = rs.getString("title");
                    long authorId = rs.getLong("author_id");
                    String authorFullName = rs.getString("author_full_name");

                    book.setId(bookId);
                    book.setTitle(title);
                    book.setAuthor(new Author(authorId, authorFullName));

                    books.put(bookId, book);
                }

                long genreId = rs.getLong("genre_id");
                String genreName = rs.getString("genre_name");
                bookGenres.add(new Genre(genreId, genreName));

                book.setGenres(bookGenres);
            }

            if(books.values().stream().findFirst().isPresent()) {
                return books.values().stream().findFirst().get();
            }
            return null;
        }
    }

    private record BookGenreRelation(long bookId, long genreId) {
    }
}
