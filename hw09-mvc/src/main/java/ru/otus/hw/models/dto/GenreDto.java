package ru.otus.hw.models.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import ru.otus.hw.models.Genre;


@AllArgsConstructor
@Data
public class GenreDto {
    private String id;

    private String name;

    public GenreDto(Genre genre) {
        this.id = genre.getId();
        this.name = genre.getName();
    }

    @Override
    public String toString() {
        return "Id: %s, Name: %s".formatted(id, name);
    }

    public Genre toDomainObject() {
        return new Genre(id, name);
    }
}
