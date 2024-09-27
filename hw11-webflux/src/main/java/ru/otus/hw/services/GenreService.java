package ru.otus.hw.services;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.otus.hw.models.dto.GenreDto;

import java.util.Set;

public interface GenreService {
    Mono<GenreDto> findById(String id);

    Flux<GenreDto> findAllByIds(Set<String> ids);

    Flux<GenreDto> findAll();

    Mono<GenreDto> insert(String name);

    Mono<GenreDto> update(String id, String name);

    Mono<Void> deleteById(String id);
}
