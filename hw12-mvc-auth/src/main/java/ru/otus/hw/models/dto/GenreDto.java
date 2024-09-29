package ru.otus.hw.models.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import ru.otus.hw.models.Genre;


@AllArgsConstructor
@Data
public class GenreDto {
    private String id;

    @NotBlank(message = "Name can not be empty")
    @Size(min = 2, max = 20, message = "Invalid name size. Expected size from 2 to 20")
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
