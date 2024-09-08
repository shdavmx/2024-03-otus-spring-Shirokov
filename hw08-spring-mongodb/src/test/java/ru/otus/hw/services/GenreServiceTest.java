package ru.otus.hw.services;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.Rollback;
import ru.otus.hw.converters.GenreConverter;
import ru.otus.hw.exceptions.EntityNotFoundException;
import ru.otus.hw.models.dto.GenreDto;

import java.util.List;

@DisplayName("Integration tests for GenreService")
@DataMongoTest
@Import({GenreServiceImpl.class, GenreConverter.class})
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class GenreServiceTest {
    private static final String GENRE_TEST_ID = "1";

    private static final int EXPECTED_GENRE_SIZE = 6;

    private static final GenreDto expectedGenre =
            new GenreDto("1", "Genre_1");

    @Autowired
    private GenreServiceImpl genreService;

    @DisplayName("should find genre by id")
    @Test
    public void shouldFindAuthorById() {
        GenreDto actualAuthor = genreService.findById(GENRE_TEST_ID);

        Assertions.assertThat(actualAuthor).isNotNull()
                .usingRecursiveComparison().isEqualTo(expectedGenre);
    }

    @DisplayName("should find all authors")
    @Test
    public void shouldFindAllAuthors() {
        List<GenreDto> actualAuthors = genreService.findAll();

        Assertions.assertThat(actualAuthors).isNotNull().hasSize(EXPECTED_GENRE_SIZE)
                .anyMatch(a -> !a.getId().isEmpty())
                .allMatch(a -> !a.getName().isEmpty());
    }

    @Rollback
    @DisplayName("should insert new author")
    @Test
    public void shouldInsertNewAuthor() {
        GenreDto actualAuthor = genreService.insert("newGenre");

        Assertions.assertThat(actualAuthor).isNotNull()
                .matches(a -> !a.getId().isEmpty())
                .matches(a -> a.getName().equals("newGenre"));
    }

    @DisplayName("should update author")
    @Test
    public void shouldUpdateAuthor() {
        GenreDto actualAuthor = genreService.update(GENRE_TEST_ID, "updatedGenre");

        Assertions.assertThat(actualAuthor).isNotNull()
                .matches(a -> !a.getId().isEmpty())
                .matches(a -> a.getName().equals("updatedGenre"));
    }

    @DisplayName("should delete author by id")
    @Test
    public void shouldDeleteAuthorById() {
        genreService.deleteById(GENRE_TEST_ID);

        Assertions.assertThatExceptionOfType(EntityNotFoundException.class)
                .isThrownBy(() -> genreService.findById(GENRE_TEST_ID));
    }
}
