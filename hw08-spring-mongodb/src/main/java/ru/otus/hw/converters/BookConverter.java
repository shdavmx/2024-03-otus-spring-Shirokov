package ru.otus.hw.converters;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.otus.hw.models.Author;
import ru.otus.hw.models.Book;
import ru.otus.hw.models.Genre;
import ru.otus.hw.models.dto.AuthorDto;
import ru.otus.hw.models.dto.BookDto;
import ru.otus.hw.models.dto.GenreDto;

import java.util.List;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toList;

@RequiredArgsConstructor
@Component
public class BookConverter {
    private final AuthorConverter authorConverter;

    private final GenreConverter genreConverter;

    public String bookToString(BookDto book) {
        String genresString = book.getGenres().stream()
                .map(genreConverter::genreToString)
                .map("{%s}"::formatted)
                .collect(Collectors.joining(","));

        return "Id: %s, title: %s, author: {%s}, genres: [%s]".formatted(
                book.getId(),
                book.getTitle(),
                authorConverter.authorToString(book.getAuthor()),
                genresString
        );
    }

    public BookDto toDto(Book book) {
        AuthorDto authorDto = authorConverter.toDto(book.getAuthor());
        List<GenreDto> genreDtos = book.getGenres().stream()
                .map(genreConverter::toDto)
                .collect(toList());
        return new BookDto(book.getId(), book.getTitle(), authorDto, genreDtos);
    }

    public Book toDbEntry(BookDto book) {
        Author author = authorConverter.toDbEntry(book.getAuthor());
        List<Genre> genres = book.getGenres().stream()
                .map(genreConverter::toDbEntry)
                .toList();

        return new Book(book.getId(), book.getTitle(), author, genres);
    }
}
