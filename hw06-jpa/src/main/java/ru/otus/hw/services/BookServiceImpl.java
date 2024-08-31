package ru.otus.hw.services;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.otus.hw.exceptions.EntityNotFoundException;
import ru.otus.hw.models.Author;
import ru.otus.hw.models.Book;
import ru.otus.hw.models.Comment;
import ru.otus.hw.models.Genre;
import ru.otus.hw.repositories.AuthorRepository;
import ru.otus.hw.repositories.BookRepository;
import ru.otus.hw.repositories.GenreRepository;

import java.util.*;

@RequiredArgsConstructor
@Service
public class BookServiceImpl implements BookService {
    private final GenreRepository genreRepository;

    private final AuthorRepository authorRepository;

    private final BookRepository bookRepository;

    @Transactional
    @Override
    public Optional<Book> findById(long id) {
        return bookRepository.findById(id);
    }

    @Transactional
    @Override
    public List<Book> findAll() {
        return bookRepository.findAll();
    }

    @Transactional
    @Override
    public Book insert(String title, long authorId, Set<Long> genresIds) {
        return save(0, title, authorId, genresIds, new ArrayList<>());
    }

    @Transactional
    @Override
    public Book update(long id, String title, long authorId, Set<Long> genresIds) {
        List<Comment> comments = findCommentsByBookId(id);
        return save(id, title, authorId, genresIds, comments);
    }

    @Override
    public List<Comment> findCommentsByBookId(long id) {
        Optional<Book> book = findById(id);
        if (book.isPresent()) {
            return book.get().getComments();
        }
        throw new EntityNotFoundException("Book with id %d not found".formatted(id));
    }

    @Transactional
    @Override
    public void deleteById(long id) {
        bookRepository.deleteById(id);
    }

    private Book save(long id, String title, long authorId, Set<Long> genresIds, List<Comment> comments) {
        if (genresIds.isEmpty()) {
            throw new IllegalArgumentException("Genres ids must not be null");
        }

        Author author = authorRepository.findById(authorId)
                .orElseThrow(() -> new EntityNotFoundException("Author with id %d not found".formatted(authorId)));
        List<Genre> genres = genreRepository.findAllByIds(genresIds);
        if (genres.isEmpty() || genresIds.size() != genres.size()) {
            throw new EntityNotFoundException("One or all genres with ids %s not found".formatted(genresIds));
        }

        Book book = new Book(id, title, author, comments, genres);
        return bookRepository.save(book);
    }
}
