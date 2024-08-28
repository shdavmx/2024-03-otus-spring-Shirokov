package ru.otus.hw.converters;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.otus.hw.models.Book;

import java.util.stream.Collectors;

@RequiredArgsConstructor
@Component
public class BookConverter {
    private final AuthorConverter authorConverter;

    private final GenreConverter genreConverter;

    private final CommentConverter commentConverter;

    public String bookToString(Book book) {
        String genresString = book.getGenres().stream()
                .map(genreConverter::genreToString)
                .map("{%s}"::formatted)
                .collect(Collectors.joining(","));

        String commentsString = book.getComments().stream()
                .map(commentConverter::commentToString)
                .map("{%s}"::formatted)
                .collect(Collectors.joining(","));

        String authorsString = book.getAuthors().stream()
                .map(authorConverter::authorToString)
                .map("{%s}"::formatted)
                .collect(Collectors.joining(","));

        return "Id: %d, title: %s, author: [%s], genres: [%s], comments: [%s]".formatted(
                book.getId(),
                book.getTitle(),
                authorsString,
                genresString,
                commentsString
        );
    }
}
