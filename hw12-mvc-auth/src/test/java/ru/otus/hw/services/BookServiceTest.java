package ru.otus.hw.services;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.annotation.DirtiesContext;
import ru.otus.hw.exceptions.EntityNotFoundException;
import ru.otus.hw.models.dto.AuthorDto;
import ru.otus.hw.models.dto.BookDto;
import ru.otus.hw.models.dto.GenreDto;

import java.util.List;
import java.util.Set;

@DisplayName("Integration tests for BookService")
@DataMongoTest
@Import({
        BookServiceImpl.class
})
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class BookServiceTest {
    private static final String TEST_BOOK_ID = "1";

    private static final int EXPECTED_BOOK_SIZE = 3;

    private final List<BookDto> testBooks = List.of(
            new BookDto("1", "Book_1",
                    new AuthorDto("1", "Author_1"),
                    List.of(
                            new GenreDto("1", "Genre_1"),
                            new GenreDto("2", "Genre_2")
                    )
            ),
            new BookDto("2", "Book_2",
                    new AuthorDto("2", "Author_2"),
                    List.of(
                            new GenreDto("3", "Genre_3"),
                            new GenreDto("4", "Genre_4")
                    )
            )
    );

    @Autowired
    private BookServiceImpl bookService;

    @DisplayName("should find expected book id")
    @Test
    public void shouldFindExpectedBookById() {
        BookDto expectedBook = testBooks.get(0);
        BookDto actualBook = bookService.findById(TEST_BOOK_ID);

        Assertions.assertThat(actualBook).isNotNull()
                .usingRecursiveComparison().isEqualTo(expectedBook);
    }

    @DisplayName("should find all books")
    @Test
    public void shouldFindAllBooks() {
        List<BookDto> actualBooks = bookService.findAll();

        Assertions.assertThat(actualBooks).isNotNull().hasSize(EXPECTED_BOOK_SIZE)
                .allMatch(b -> !b.getId().isEmpty())
                .allMatch(b -> !b.getTitle().isEmpty())
                .allMatch(b -> b.getAuthor() != null)
                .allMatch(b -> b.getGenres() != null && !b.getGenres().isEmpty());
    }

    @DisplayName("should insert new book")
    @Test
    public void shouldInsertNewBook() {
        BookDto expectedBook = new BookDto(
                "4", "new book",
                new AuthorDto("1", "Author_1"),
                List.of(
                        new GenreDto("1", "Genre_1"),
                        new GenreDto("2", "Genre_2"))
        );
        BookDto actualBook = bookService.insert(expectedBook.getTitle(), "1", List.of("1", "2"));

        Assertions.assertThat(actualBook).isNotNull()
                .usingRecursiveComparison().ignoringFields("id").isEqualTo(expectedBook);
    }

    @DisplayName("should update existing book")
    @Test
    public void shouldUpdateExistingBook() {
        BookDto expectedBook = new BookDto(
                "1", "new book",
                new AuthorDto("1", "Author_1"),
                List.of(
                        new GenreDto("1", "Genre_1"),
                        new GenreDto("2", "Genre_2"))
        );
        BookDto actualBook = bookService
                .update(expectedBook.getId(), expectedBook.getTitle(), "1", List.of("1", "2"));

        Assertions.assertThat(actualBook).isNotNull()
                .usingRecursiveComparison().isEqualTo(expectedBook);
    }

    @DisplayName("should delete book by id")
    @Test
    public void shouldDeleteBookById() {
        bookService.deleteById(TEST_BOOK_ID);

        Assertions.assertThatExceptionOfType(EntityNotFoundException.class)
                .isThrownBy(() -> bookService.findById(TEST_BOOK_ID));
    }
}
