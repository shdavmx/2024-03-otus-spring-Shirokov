package ru.otus.hw.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.otus.hw.exceptions.EntityNotFoundException;
import ru.otus.hw.models.Author;
import ru.otus.hw.models.Book;
import ru.otus.hw.models.Genre;
import ru.otus.hw.models.dto.BookDto;
import ru.otus.hw.repositories.AuthorRepository;
import ru.otus.hw.repositories.BookRepository;
import ru.otus.hw.repositories.CommentRepository;
import ru.otus.hw.repositories.GenreRepository;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class BookServiceImpl implements BookService {
    private final BookRepository bookRepository;

    private final AuthorRepository authorRepository;

    private final GenreRepository genreRepository;

    private final CommentRepository commentRepository;

    @Override
    public BookDto findById(String id) {
        Optional<Book> book = bookRepository.findById(id);
        if (book.isPresent()) {
            return BookDto.fromDomainObject(book.get());
        }
        throw new EntityNotFoundException("Book with id '%s' not found".formatted(id));
    }

    @Override
    public List<BookDto> findAll() {
        return bookRepository.findAll().stream()
                .map(BookDto::fromDomainObject)
                .toList();
    }

    @Override
    public List<BookDto> findAllByAuthorId(String authorId) {
        return bookRepository.findAllByAuthorId(authorId).stream()
                .map(BookDto::fromDomainObject)
                .toList();
    }

    @Override
    public BookDto insert(String title, String authorId, List<String> genresIds) {
        return save(null, title, authorId, genresIds);
    }

    @Override
    public BookDto update(String id, String title, String authorId, List<String> genresIds) {
        return save(id, title, authorId, genresIds);
    }

    @Override
    public void deleteById(String id) {
        if (!bookRepository.existsById(id)) {
            throw new EntityNotFoundException("Book with id '%s' not found".formatted(id));
        }

        bookRepository.deleteById(id);
        commentRepository.deleteAllByBookId(id);
    }

    private BookDto save(String id, String title, String authorId, List<String> genresIds) {
        if (genresIds.isEmpty()) {
            throw new IllegalArgumentException("Genres ids must not be null");
        }

        Author author = authorRepository.findById(authorId)
                .orElseThrow(() -> new EntityNotFoundException("Author with id %s not found".formatted(authorId)));
        List<Genre> genres = genreRepository.findAllById(genresIds);
        if (genres.isEmpty() || genresIds.size() != genres.size()) {
            throw new EntityNotFoundException("One or all genres with ids %s not found".formatted(genresIds));
        }

        Book book = new Book(id, title, author, genres);
        return BookDto.fromDomainObject(bookRepository.save(book));
    }
}
