package ru.otus.hw.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.otus.hw.exceptions.EntityNotFoundException;
import ru.otus.hw.models.Author;
import ru.otus.hw.models.dto.AuthorDto;
import ru.otus.hw.repositories.AuthorRepository;
import ru.otus.hw.repositories.BookRepository;

import java.util.Optional;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class AuthorServiceImpl implements AuthorService {
    private final AuthorRepository authorRepository;

    private final BookRepository bookRepository;

    @Override
    public Flux<AuthorDto> findAll() {
        return authorRepository.findAll()
                .map(AuthorDto::fromDomainObject)
                .switchIfEmpty(Flux.empty());
    }

    @Override
    public Mono<AuthorDto> findById(String id) {
        return authorRepository.findById(id)
                .map(AuthorDto::fromDomainObject)
                .switchIfEmpty(Mono.error(new EntityNotFoundException("Author '%s' not found".formatted(id))));
    }

    @Override
    public Mono<AuthorDto> insert(String fullName) {
        return authorRepository.insert(new Author(fullName))
                .map(AuthorDto::fromDomainObject);
    }

    @Override
    public Mono<AuthorDto> update(String id, String name) {
        return authorRepository.save(new Author(id, name))
                .map(AuthorDto::fromDomainObject);
    }

    @Override
    public Mono<Void> deleteById(String id) {
        bookRepository.deleteAllByAuthorId(id);
        return authorRepository.deleteById(id);
    }
}
