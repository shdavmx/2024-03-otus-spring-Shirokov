package ru.otus.hw.converters;

import org.springframework.stereotype.Component;
import ru.otus.hw.models.Genre;
import ru.otus.hw.models.dto.GenreDto;

@Component
public class GenreConverter {
    public String genreToString(GenreDto genre) {
        return "Id: %d, Name: %s".formatted(genre.getId(), genre.getName());
    }

    public GenreDto toDto(Genre genre) {
        return new GenreDto(genre.getId(), genre.getName());
    }
}