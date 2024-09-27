package ru.otus.hw.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.otus.hw.exceptions.EntityNotFoundException;
import ru.otus.hw.models.Genre;
import ru.otus.hw.models.dto.GenreDto;
import ru.otus.hw.repositories.BookRepositoryCustom;
import ru.otus.hw.repositories.GenreRepository;

import java.util.List;
import java.util.Set;

@RequiredArgsConstructor
@Service
public class GenreServiceImpl implements GenreService {
    private final GenreRepository genreRepository;

    private final BookRepositoryCustom bookRepositoryCustom;

    @Override
    public Mono<GenreDto> findById(String id) {
        return genreRepository.findById(id)
                .map(GenreDto::fromDomainObject)
                .switchIfEmpty(Mono.error(
                        new EntityNotFoundException("Genre with id '%s' not found".formatted(id))));
    }

    @Override
    public Flux<GenreDto> findAllByIds(Set<String> ids) {
        return genreRepository.findAllById(ids)
                .map(GenreDto::fromDomainObject)
                .switchIfEmpty(Flux.empty());
    }

    @Override
    public Flux<GenreDto> findAll() {
        return genreRepository.findAll()
                .map(GenreDto::fromDomainObject)
                .switchIfEmpty(Flux.empty());
    }

    @Override
    public Mono<GenreDto> insert(String name) {
        return genreRepository.insert(new Genre(name))
                .map(GenreDto::fromDomainObject);
    }

    @Override
    public Mono<GenreDto> update(String id, String name) {
        return genreRepository.save(new Genre(id, name))
                .map(GenreDto::fromDomainObject);
    }

    @Override
    public Mono<Void> deleteById(String id) {
        return bookRepositoryCustom.removeGenreArrayElementsById(id)
                .flatMap(b -> genreRepository.deleteById(id));
    }
}
