package ru.otus.hw.services;

import ru.otus.hw.models.dto.BookDto;

import java.util.List;
import java.util.Set;

public interface BookService {
    BookDto findById(String id);

    List<BookDto> findAll();

    List<BookDto> findAllByAuthorId(String authorId);

    BookDto insert(String title, String authorId, Set<String> genresIds);

    BookDto update(String id, String title, String authorId, Set<String> genresIds);

    void deleteById(String id);

    void deleteAllByAuthorId(String authorId);
}
