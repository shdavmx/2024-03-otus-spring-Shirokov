package ru.otus.hw.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.hw.converters.BookToDtoConverter;
import ru.otus.hw.exceptions.EntityNotFoundException;
import ru.otus.hw.models.Author;
import ru.otus.hw.models.Book;
import ru.otus.hw.models.Genre;
import ru.otus.hw.models.dto.BookDto;
import ru.otus.hw.repositories.AuthorRepository;
import ru.otus.hw.repositories.BookRepository;
import ru.otus.hw.repositories.GenreRepository;

import java.util.*;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class BookServiceImpl implements BookService {
    private final GenreRepository genreRepository;

    private final AuthorRepository authorRepository;

    private final BookRepository bookRepository;

    @Transactional(readOnly = true)
    @Override
    public BookDto findById(long id) {
        Optional<Book> book = bookRepository.findById(id);
        if(book.isPresent()) {
            return BookToDtoConverter.toDto(book.get());
        }
        throw new EntityNotFoundException("Book with id %d not found".formatted(id));
    }

    @Transactional(readOnly = true)
    @Override
    public List<BookDto> findAll() {
        return bookRepository.findAll().stream()
                .map(BookToDtoConverter::toDto)
                .collect(Collectors.toList());
    }

    @Transactional
    @Override
    public BookDto insert(String title, long authorId, Set<Long> genresIds) {
        return save(0, title, authorId, genresIds);
    }

    @Transactional
    @Override
    public BookDto update(long id, String title, long authorId, Set<Long> genresIds) {
        return save(id, title, authorId, genresIds);
    }

    @Transactional
    @Override
    public void deleteById(long id) {
        bookRepository.deleteById(id);
    }

    private BookDto save(long id, String title, long authorId, Set<Long> genresIds) {
        if (genresIds.isEmpty()) {
            throw new IllegalArgumentException("Genres ids must not be null");
        }

        Author author = authorRepository.findById(authorId)
                .orElseThrow(() -> new EntityNotFoundException("Author with id %d not found".formatted(authorId)));
        List<Genre> genres = genreRepository.findAllByIds(genresIds);
        if (genres.isEmpty() || genresIds.size() != genres.size()) {
            throw new EntityNotFoundException("One or all genres with ids %s not found".formatted(genresIds));
        }

        Book book = new Book(id, title, author, genres);
        return BookToDtoConverter.toDto(bookRepository.save(book));
    }
}
