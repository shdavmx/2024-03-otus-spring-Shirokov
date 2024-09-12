package ru.otus.hw.models.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import ru.otus.hw.models.Genre;


@AllArgsConstructor
@Data
public class GenreDto {
    private String id;

    private String name;

    @Override
    public String toString() {
        return "Id: %s, Name: %s".formatted(id, name);
    }

    public Genre toDomainObject() {
        return new Genre(id, name);
    }

    public static GenreDto fromDomainObject(Genre genre) {
        return new GenreDto(genre.getId(), genre.getName());
    }
}
