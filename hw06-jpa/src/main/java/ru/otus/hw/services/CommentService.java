package ru.otus.hw.services;

import ru.otus.hw.models.Comment;

import java.util.Optional;

public interface CommentService {
    Optional<Comment> findById(long id);
}
