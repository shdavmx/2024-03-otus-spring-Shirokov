package ru.otus.hw.converters;

import ru.otus.hw.models.Book;
import ru.otus.hw.models.dto.BookDto;

public class BookToDtoConverter {

    public static BookDto toDto(Book book) {
        return new BookDto(book.getId(), book.getTitle(), book.getAuthor(), book.getGenres());
    }
}
