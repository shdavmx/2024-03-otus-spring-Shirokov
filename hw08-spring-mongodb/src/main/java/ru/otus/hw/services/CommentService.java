package ru.otus.hw.services;

import ru.otus.hw.models.dto.CommentDto;

import java.util.List;

public interface CommentService {
    List<CommentDto> findAllByBookId(String bookId);

    CommentDto insert(String comment, String bookId);

    CommentDto update(String oldComment, String newComment);

    void deleteById(String id);
}
