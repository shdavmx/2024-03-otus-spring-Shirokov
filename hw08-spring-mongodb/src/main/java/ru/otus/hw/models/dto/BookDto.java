package ru.otus.hw.models.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@AllArgsConstructor
@Data
public class BookDto {
    private String id;

    private String title;

    private AuthorDto author;

    private List<GenreDto> genres;
}
