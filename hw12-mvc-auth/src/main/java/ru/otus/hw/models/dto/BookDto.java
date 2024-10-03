package ru.otus.hw.models.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import ru.otus.hw.models.Book;
import ru.otus.hw.models.BookFormModel;

import java.util.List;
import java.util.stream.Collectors;

@AllArgsConstructor
@Data
public class BookDto {
    private String id;

    @NotBlank(message = "Title can not be empty")
    @Size(min = 2, max = 20, message = "Invalid title size. Expected size from 2 to 20")
    private String title;

    private AuthorDto author;

    private List<GenreDto> genres;

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

    public BookFormModel toFormModel() {
        return new BookFormModel(id, title, author.getId(),
                genres.stream().map(GenreDto::getId).toList());
    }

    public static BookDto fromDomainObject(Book book) {
        return new BookDto(book.getId(), book.getTitle(),
                AuthorDto.fromDomainObject(book.getAuthor()),
                book.getGenres().stream().map(GenreDto::fromDomainObject).toList());
    }
}
