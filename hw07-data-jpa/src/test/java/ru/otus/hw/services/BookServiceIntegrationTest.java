package ru.otus.hw.services;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.hw.converters.AuthorConverter;
import ru.otus.hw.converters.BookConverter;
import ru.otus.hw.converters.GenreConverter;
import ru.otus.hw.exceptions.EntityNotFoundException;
import ru.otus.hw.models.dto.AuthorDto;
import ru.otus.hw.models.dto.BookDto;
import ru.otus.hw.models.dto.GenreDto;

import java.util.List;
import java.util.Set;

@DisplayName("Tests for BookService")
@DataJpaTest
@Import({
        BookServiceImpl.class,
        BookConverter.class,
        AuthorConverter.class,
        GenreConverter.class
})
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_CLASS)
public class BookServiceIntegrationTest {
    private static final long TEST_BOOK_ID = 1L;

    private static final int EXPECTED_BOOK_SIZE = 3;

    private final List<BookDto> testBooks = List.of(
            new BookDto(1, "BookTitle_1",
                    new AuthorDto(1, "Author_1"),
                    List.of(
                            new GenreDto(1, "Genre_1"),
                            new GenreDto(2, "Genre_2")
                    )
            ),
            new BookDto(2, "BookTitle_2",
                    new AuthorDto(2, "Author_2"),
                    List.of(
                            new GenreDto(3, "Genre_3"),
                            new GenreDto(4, "Genre_4")
                    )
            )
    );

    @Autowired
    private BookServiceImpl bookService;

    @Transactional(propagation = Propagation.NEVER)
    @DisplayName("should find expected book id")
    @Test
    public void shouldFindExpectedBookById() {
        BookDto expectedBook = testBooks.get(0);
        BookDto actualBook = bookService.findById(TEST_BOOK_ID);

        Assertions.assertThat(actualBook).isNotNull()
                .usingRecursiveComparison().isEqualTo(expectedBook);
    }

    @Transactional(propagation = Propagation.NEVER)
    @DisplayName("should find all books")
    @Test
    public void shouldFindAllBooks() {
        List<BookDto> actualBooks = bookService.findAll();

        Assertions.assertThat(actualBooks).isNotNull().hasSize(EXPECTED_BOOK_SIZE)
                .allMatch(b -> b.getId() > 0)
                .allMatch(b -> !b.getTitle().isEmpty())
                .allMatch(b -> b.getAuthor() != null)
                .allMatch(b -> b.getGenres() != null && !b.getGenres().isEmpty());
    }

    @Rollback
    @DisplayName("should insert new book")
    @Test
    public void shouldInsertNewBook() {
        BookDto expectedBook = new BookDto(
                4, "new book",
                new AuthorDto(1, "Author_1"),
                List.of(
                        new GenreDto(1, "Genre_1"),
                        new GenreDto(2, "Genre_2"))
        );
        BookDto actualBook = bookService.insert(expectedBook.getTitle(), 1, Set.of(1L, 2L));

        Assertions.assertThat(actualBook).isNotNull()
                .usingRecursiveComparison().isEqualTo(expectedBook);
    }

    @Rollback
    @DisplayName("should update existing book")
    @Test
    public void shouldUpdateExistingBook() {
        BookDto expectedBook = new BookDto(
                1, "new book",
                new AuthorDto(1, "Author_1"),
                List.of(
                        new GenreDto(1, "Genre_1"),
                        new GenreDto(2, "Genre_2"))
        );
        BookDto actualBook = bookService
                .update(expectedBook.getId(), expectedBook.getTitle(), 1, Set.of(1L, 2L));

        Assertions.assertThat(actualBook).isNotNull()
                .usingRecursiveComparison().isEqualTo(expectedBook);
    }

    @Rollback
    @DisplayName("should delete book by id")
    @Test
    public void shouldDeleteBookById() {
        bookService.deleteById(TEST_BOOK_ID);

        Assertions.assertThatExceptionOfType(EntityNotFoundException.class)
                .isThrownBy(() -> bookService.findById(TEST_BOOK_ID));
    }
}
