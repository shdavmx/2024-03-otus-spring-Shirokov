package ru.otus.hw.services;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.Rollback;
import ru.otus.hw.exceptions.EntityNotFoundException;
import ru.otus.hw.models.dto.AuthorDto;

import java.util.List;

@DisplayName("Integration tests for AuthorService")
@DataMongoTest
@Import({AuthorServiceImpl.class})
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class AuthorServiceTest {
    private static final String TEST_AUTHOR_ID = "1";

    private static final int EXPECTED_AUTHOR_SIZE = 6;

    private static final AuthorDto expectedAuthor =
            new AuthorDto("1", "Author_1");

    @Autowired
    private AuthorServiceImpl authorService;

    @DisplayName("should find author by id")
    @Test
    public void shouldFindAuthorById() {
        AuthorDto actualAuthor = authorService.findById(TEST_AUTHOR_ID);

        Assertions.assertThat(actualAuthor).isNotNull()
                .usingRecursiveComparison().isEqualTo(expectedAuthor);
    }

    @DisplayName("should find all authors")
    @Test
    public void shouldFindAllAuthors() {
        List<AuthorDto> actualAuthors = authorService.findAll();

        Assertions.assertThat(actualAuthors).isNotNull().hasSize(EXPECTED_AUTHOR_SIZE)
                .anyMatch(a -> !a.getId().isEmpty())
                .allMatch(a -> !a.getFullName().isEmpty());
    }

    @Rollback
    @DisplayName("should insert new author")
    @Test
    public void shouldInsertNewAuthor() {
        AuthorDto actualAuthor = authorService.insert("newAuthor");

        Assertions.assertThat(actualAuthor).isNotNull()
                .matches(a -> !a.getId().isEmpty())
                .matches(a -> a.getFullName().equals("newAuthor"));
    }

    @DisplayName("should update author")
    @Test
    public void shouldUpdateAuthor() {
        AuthorDto actualAuthor = authorService.update(TEST_AUTHOR_ID, "updatedAuthor");

        Assertions.assertThat(actualAuthor).isNotNull()
                .matches(a -> !a.getId().isEmpty())
                .matches(a -> a.getFullName().equals("updatedAuthor"));
    }

    @DisplayName("should delete author by id")
    @Test
    public void shouldDeleteAuthorById() {
        authorService.deleteById(TEST_AUTHOR_ID);

        Assertions.assertThatExceptionOfType(EntityNotFoundException.class)
                .isThrownBy(() -> authorService.findById(TEST_AUTHOR_ID));
    }
}
