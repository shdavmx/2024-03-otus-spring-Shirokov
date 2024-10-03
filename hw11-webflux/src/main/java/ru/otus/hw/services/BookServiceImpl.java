package ru.otus.hw.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.otus.hw.exceptions.EntityNotFoundException;
import ru.otus.hw.models.Book;
import ru.otus.hw.models.dto.BookDto;
import ru.otus.hw.repositories.AuthorRepository;
import ru.otus.hw.repositories.BookRepository;
import ru.otus.hw.repositories.CommentRepository;
import ru.otus.hw.repositories.GenreRepository;

import java.util.List;

@RequiredArgsConstructor
@Service
public class BookServiceImpl implements BookService {
    private final BookRepository bookRepository;

    private final AuthorRepository authorRepository;

    private final GenreRepository genreRepository;

    private final CommentRepository commentRepository;

    @Override
    public Mono<BookDto> findById(String id) {
        return bookRepository.findById(id)
                .map(BookDto::fromDomainObject)
                .switchIfEmpty(Mono.error(new EntityNotFoundException("Book with id '%s' not found".formatted(id))));
    }

    @Override
    public Flux<BookDto> findAll() {
        return bookRepository.findAll()
                .map(BookDto::fromDomainObject)
                .switchIfEmpty(Flux.empty());
    }

    @Override
    public Flux<BookDto> findAllByAuthorId(String authorId) {
        return bookRepository.findAllByAuthorId(authorId)
                .map(BookDto::fromDomainObject);
    }

    @Override
    public Mono<BookDto> insert(String title, String authorId, List<String> genresIds) {
        return save(null, title, authorId, genresIds);
    }

    @Override
    public Mono<BookDto> update(String id, String title, String authorId, List<String> genresIds) {
        return save(id, title, authorId, genresIds);
    }

    @Override
    public Mono<Void> deleteById(String id) {
        return bookRepository.deleteById(id)
                .and(commentRepository.deleteAllByBookId(id));
    }

    private Mono<BookDto> save(String id, String title, String authorId, List<String> genresIds) {
        if (genresIds.isEmpty()) {
            throw new IllegalArgumentException("Genres ids must not be null");
        }

        return Mono.zip(authorRepository.findById(authorId), genreRepository.findAllById(genresIds).collectList())
                .flatMap(data -> bookRepository.save(new Book(id, title, data.getT1(), data.getT2()))
                        .map(BookDto::fromDomainObject));
    }
}
