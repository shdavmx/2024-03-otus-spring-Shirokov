package ru.otus.hw.converters;

import ru.otus.hw.models.Genre;
import ru.otus.hw.models.dto.GenreDto;

public class GenreToDtoConverter {
    public static GenreDto toDto(Genre genre) {
        return new GenreDto(genre.getId(), genre.getName());
    }
}
