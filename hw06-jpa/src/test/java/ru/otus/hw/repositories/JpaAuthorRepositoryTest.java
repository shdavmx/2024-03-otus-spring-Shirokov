package ru.otus.hw.repositories;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.Import;
import ru.otus.hw.models.Author;
import ru.otus.hw.repositories.JpaAuthorRepository;

import java.util.List;
import java.util.Optional;

@DisplayName("Tests for JpaAuthorRepository")
@DataJpaTest
@Import(JpaAuthorRepository.class)
public class JpaAuthorRepositoryTest {
    private static final long TEST_AUTHOR_ID = 1L;

    private static final int EXPECTED_AUTHOR_SIZE = 3;

    @Autowired
    private JpaAuthorRepository jpaAuthorRepository;

    @Autowired
    private TestEntityManager testEntityManager;

    @DisplayName("should find expected author by id")
    @Test
    public void shouldFindExpectedAuthorById() {
        Optional<Author> actualAuthor = jpaAuthorRepository.findById(TEST_AUTHOR_ID);
        Author expectedAuthor = testEntityManager.find(Author.class, TEST_AUTHOR_ID);

        Assertions.assertThat(actualAuthor).isPresent().get()
                .usingRecursiveComparison().isEqualTo(expectedAuthor);
    }

    @DisplayName("should find all expected authors")
    @Test
    public void shouldFindAllExpectedAuthors() {
        List<Author> actualAuthorList = jpaAuthorRepository.findAll();

        Assertions.assertThat(actualAuthorList).isNotNull().hasSize(EXPECTED_AUTHOR_SIZE)
                .allMatch(a -> a.getId() > 0)
                .allMatch(a -> !a.getFullName().isEmpty());
    }
}
