package ru.otus.hw.services;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.otus.hw.models.dto.BookDto;

import java.util.List;

public interface BookService {
    Mono<BookDto> findById(String id);

    Flux<BookDto> findAll();

    Flux<BookDto> findAllByAuthorId(String authorId);

    Mono<BookDto> insert(String title, String authorId, List<String> genresIds);

    Mono<BookDto> update(String id, String title, String authorId, List<String> genresIds);

    Mono<Void> deleteById(String id);
}
