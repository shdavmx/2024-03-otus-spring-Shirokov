package ru.otus.hw.converters;

import ru.otus.hw.models.Book;
import ru.otus.hw.models.dto.AuthorDto;
import ru.otus.hw.models.dto.BookDto;
import ru.otus.hw.models.dto.GenreDto;

import java.util.List;
import java.util.stream.Collectors;

public class BookToDtoConverter {

    public static BookDto toDto(Book book) {
        AuthorDto authorDto = AuthorToDtoConverter.toDto(book.getAuthor());
        List<GenreDto> genreDtos = book.getGenres().stream()
                .map(GenreToDtoConverter::toDto)
                .collect(Collectors.toList());
        return new BookDto(book.getId(), book.getTitle(), authorDto, genreDtos);
    }
}
