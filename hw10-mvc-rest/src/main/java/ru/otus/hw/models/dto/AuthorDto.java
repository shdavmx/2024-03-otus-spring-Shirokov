package ru.otus.hw.models.dto;

import lombok.Data;
import ru.otus.hw.models.Author;

@Data
public class AuthorDto {
    private String id;

    private String fullName;

    public AuthorDto(String id, String fullName) {
        this.id = id;
        this.fullName = fullName;
    }

    @Override
    public String toString() {
        return "Id: %s, FullName: %s".formatted(id, fullName);
    }

    public Author toDomainObject() {
        return new Author(id, fullName);
    }

    public static AuthorDto fromDomainObject(Author author) {
        return new AuthorDto(author.getId(), author.getFullName());
    }
}
