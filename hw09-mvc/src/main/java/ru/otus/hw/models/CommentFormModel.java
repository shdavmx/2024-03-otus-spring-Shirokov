package ru.otus.hw.models;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class CommentFormModel {
    private String comment;

    private String bookId;
}
