package ru.otus.hw.services;

import ru.otus.hw.models.dto.CommentDto;

import java.util.List;

public interface CommentService {
    CommentDto findById(String id);

    List<CommentDto> findAllByBookId(String bookId);

    CommentDto insert(String commentText, String bookId);

    CommentDto update(String id, String commentText, String bookId);

    void deleteById(String id);
}
