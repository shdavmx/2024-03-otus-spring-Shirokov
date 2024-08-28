package ru.otus.hw.repositories;

import ru.otus.hw.models.Book;
import ru.otus.hw.models.Comment;

import java.util.List;
import java.util.Optional;

public interface BookRepository {
    Optional<Book> findById(long id);

    List<Book> findAll();

    List<Comment> findCommentsByBookId(long id);

    Book save(Book book);

    void deleteById(long id);
}
