package ru.otus.hw.repositories;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.annotation.DirtiesContext;
import ru.otus.hw.models.Book;
import ru.otus.hw.models.Genre;

import java.util.List;

@DisplayName("Tests for BookServiceCustom")
@DataMongoTest
@Import({BookRepositoryCustomImpl.class})
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class BookServiceCustomTest {
    private static final String TEST_GENRE_ID = "1";

    private final List<Genre> expectedGenres = List.of(
            new Genre("2", "Genre_2")
    );

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private BookRepositoryCustomImpl bookRepositoryCustom;

    @DisplayName("should remove dbref from book genres")
    @Test
    public void shouldRemoveDbRefFromBookGenres() {
        bookRepositoryCustom.removeGenreArrayElementsById(TEST_GENRE_ID);
        List<Book> actualBooks = bookRepository.findAll();

        Assertions.assertThat(actualBooks)
                .allMatch(b -> b.getGenres().stream()
                        .noneMatch(g -> g.getId().equals(TEST_GENRE_ID)));
    }
}
