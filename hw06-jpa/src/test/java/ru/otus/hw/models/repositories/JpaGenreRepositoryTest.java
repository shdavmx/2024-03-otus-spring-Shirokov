package ru.otus.hw.models.repositories;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.Import;
import ru.otus.hw.models.Genre;
import ru.otus.hw.repositories.JpaGenreRepository;

import java.util.List;
import java.util.Set;

@DisplayName("Tests for JpaGenreRepository")
@DataJpaTest
@Import(JpaGenreRepository.class)
public class JpaGenreRepositoryTest {
    private static final Set<Long> TEST_GENRE_IDS = Set.of(1L, 2L, 3L);

    private static final int ALL_EXPECTED_GENRE_BY_IDS_SIZE = 3;

    private static final int ALL_EXPECTED_GENRE_SIZE = 6;

    @Autowired
    private JpaGenreRepository jpaGenreRepository;

    @Autowired
    private TestEntityManager testEntityManager;

    @DisplayName("should find all expected genres")
    @Test
    public void shouldFindAllExpectedGenres() {
        List<Genre> actualGenreList = jpaGenreRepository.findAll();

        Assertions.assertThat(actualGenreList).isNotNull().hasSize(ALL_EXPECTED_GENRE_SIZE)
                .allMatch(a -> a.getId() > 0)
                .allMatch(a -> !a.getName().isEmpty());
    }

    @DisplayName("should find all expected genres by ids")
    @Test
    public void shouldFindAllExpectedGenresByIds() {
        List<Genre> actualGenreList = jpaGenreRepository.findAllByIds(TEST_GENRE_IDS);

        Assertions.assertThat(actualGenreList).isNotNull().hasSize(ALL_EXPECTED_GENRE_BY_IDS_SIZE)
                .allMatch(a -> a.getId() > 0)
                .allMatch(a -> !a.getName().isEmpty());
    }
}
