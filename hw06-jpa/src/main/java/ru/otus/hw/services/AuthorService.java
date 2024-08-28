package ru.otus.hw.services;

import ru.otus.hw.models.Author;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface AuthorService {
    List<Author> findAll();

    Optional<Author> findById(long id);

    List<Author> findByIds(Set<Long> ids);
}
