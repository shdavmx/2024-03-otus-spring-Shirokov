package ru.otus.hw.repositories;

import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;
import org.springframework.stereotype.Repository;
import ru.otus.hw.models.Author;
import ru.otus.hw.models.Book;
import ru.otus.hw.models.Genre;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

@Repository
@RequiredArgsConstructor
public class JdbcBookRepository implements BookRepository {
    private final NamedParameterJdbcOperations jdbc;

    private final GenreRepository genreRepository;

    @Override
    public Optional<Book> findById(long id) {
        Map<String, Object> param = Collections.singletonMap("id", id);
        Book book = jdbc.query("select b.id as book_id, b.title as title, " +
                "                          a.id as author_id, a.full_name as author_full_name, " +
                "                          g.id as genre_id, g.name as genre_name " +
                "from books b " +
                "join authors as a on b.author_id = a.id " +
                "join books_genres as bg on b.id = bg.book_id " +
                "join genres as g on bg.genre_id = g.id " +
                "where b.id = :id", param, new BookResultSetExtractor());

        return Optional.ofNullable(book);
    }

    @Override
    public List<Book> findAll() {
        List<Genre> genres = genreRepository.findAll();
        List<BookGenreRelation> bookGenreRelations = getAllGenreRelations();
        List<Book> books = getAllBooksWithoutGenres();

        for(Book book: books) {
            List<Genre> bookGenres = new ArrayList<>();
            for(BookGenreRelation relation: bookGenreRelations) {
                if(relation.bookId == book.getId()) {
                    Optional<Genre> genre = genres.stream()
                            .filter(g -> g.getId() == relation.genreId)
                            .findFirst();

                    genre.ifPresent(bookGenres::add);
                }
            }
            book.setGenres(bookGenres);
        }

        return books;
    }

    @Override
    public Book save(Book book) {
        return null;
    }

    @Override
    public void deleteById(long id) {

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
            while (rs.next()) {
                Book book = new Book();

                long genreId = rs.getLong("genre_id");
                String genreName = rs.getString("genre_name");
                Genre genre = new Genre(genreId, genreName);

                long bookId = rs.getLong("book_id");
                if(books.containsKey(bookId)) {
                    book = books.get(bookId);
                    List<Genre> genreList = book.getGenres();
                    if(genreList == null) {
                        book.setGenres(List.of(genre));
                    }
                    else {
                        List<Genre> newGenreList = new ArrayList<>(genreList);
                        newGenreList.add(genre);
                        book.setGenres(newGenreList);
                    }
                }
                else {
                    String title = rs.getString("title");
                    long authorId = rs.getLong("author_id");
                    String authorFullName = rs.getString("author_full_name");

                    book.setId(bookId);
                    book.setTitle(title);
                    book.setAuthor(new Author(authorId, authorFullName));
                    book.setGenres(List.of(genre));

                    books.put(bookId, book);
                }
            }

            return books.get(books.keySet()
                    .stream()
                    .findFirst()
                    .orElseThrow());
        }
    }

    private record BookGenreRelation(long bookId, long genreId) {
    }
}