package ru.otus.hw.models.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class AuthorDto {
    private String id;

    private String fullName;
}
