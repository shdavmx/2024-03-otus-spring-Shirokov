package ru.otus.hw.services;

import ru.otus.hw.models.dto.GenreDto;

import java.util.List;

public interface GenreService {
    GenreDto findByName(String name);

    List<GenreDto> findAll();

    GenreDto insert(String name);

    GenreDto update(String oldName, String newName);

    void deleteByName(String name);
}
