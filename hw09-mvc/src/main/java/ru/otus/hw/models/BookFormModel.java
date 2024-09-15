package ru.otus.hw.models;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@AllArgsConstructor
@Data
public class BookFormModel {
    private String id;

    private String title;

    private String authorId;

    private List<String> genreIds;
}
