package ru.otus.hw.models.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import ru.otus.hw.models.Book;

import java.util.List;
import java.util.stream.Collectors;

@AllArgsConstructor
@Data
public class BookDto {
    private String id;

    private String title;

    private AuthorDto author;

    private List<GenreDto> genres;

    public BookDto(Book book) {
        this.id = book.getId();
        this.title = book.getTitle();
        this.author = new AuthorDto(book.getAuthor());
        this.genres = book.getGenres().stream().map(GenreDto::new).toList();
    }

    @Override
    public String toString() {
        String genreString = genres.stream()
                .map(GenreDto::toString)
                .map("{%s}"::formatted)
                .collect(Collectors.joining(","));

        return "Id: %s, title: %s, author: {%s}, genres: [%s]".formatted(
                id, title, author.toString(), genreString);
    }

    public Book toDomainObject() {
        return new Book(id, title,
                author.toDomainObject(),
                genres.stream().map(GenreDto::toDomainObject).toList());
    }

    public String genresString() {
        return genres.stream()
                .map(GenreDto::getName)
                .collect(Collectors.joining(","));
    }
}
