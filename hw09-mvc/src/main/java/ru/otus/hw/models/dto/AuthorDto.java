package ru.otus.hw.models.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import ru.otus.hw.models.Author;

@AllArgsConstructor
@Data
public class AuthorDto {
    private String id;

    private String fullName;

    public AuthorDto(Author author) {
        this.id = author.getId();
        this.fullName = author.getFullName();
    }

    @Override
    public String toString() {
        return "Id: %s, FullName: %s".formatted(id, fullName);
    }

    public Author toDomainObject() {
        return new Author(id, fullName);
    }
}
