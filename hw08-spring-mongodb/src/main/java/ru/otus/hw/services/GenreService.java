package ru.otus.hw.services;

import ru.otus.hw.models.dto.GenreDto;

import java.util.List;
import java.util.Set;

public interface GenreService {
    GenreDto findById(String id);

    List<GenreDto> findAllByIds(Set<String> ids);

    List<GenreDto> findAll();

    GenreDto insert(String name);

    GenreDto update(String id, String name);

    void deleteById(String id);
}
