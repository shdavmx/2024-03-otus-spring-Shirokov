package ru.otus.hw.converters;

import ru.otus.hw.models.Author;
import ru.otus.hw.models.dto.AuthorDto;

public class AuthorToDtoConverter {
    public static AuthorDto toDto(Author author) {
        return new AuthorDto(author.getId(), author.getFullName());
    }
}
