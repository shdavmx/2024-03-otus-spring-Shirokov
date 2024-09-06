package ru.otus.hw.services;

import ru.otus.hw.models.dto.GenreDto;

import java.util.List;
import java.util.Set;

public interface GenreService {
    List<GenreDto> findAll();

    List<GenreDto> findAllByIds(Set<Long> ids);
}
