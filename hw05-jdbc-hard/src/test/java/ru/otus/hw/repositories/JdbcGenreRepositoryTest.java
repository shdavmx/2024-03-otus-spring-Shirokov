package ru.otus.hw.repositories;

import org.assertj.core.api.Assertions;
import org.assertj.core.api.recursive.comparison.RecursiveComparisonConfiguration;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;
import ru.otus.hw.models.Genre;

import java.util.List;
import java.util.Set;

@DisplayName("Test for JdbcGenreRepository")
@JdbcTest
@Import({JdbcGenreRepository.class})
public class JdbcGenreRepositoryTest {
    @Autowired
    private JdbcGenreRepository jdbcGenreRepository;

    private final Set<Long> genreIds = Set.of(1L, 2L ,3L);

    @DisplayName("should return all correct genres")
    @Test
    public void shouldReturnAllCorrectGenres() {
        RecursiveComparisonConfiguration configuration = RecursiveComparisonConfiguration.builder()
                .build();

        List<Genre> actualGenres = jdbcGenreRepository.findAll();
        List<Genre> expectedGenres = List.of(new Genre(1, "Genre_1"),
                new Genre(2, "Genre_2"),
                new Genre(3, "Genre_3"),
                new Genre(4, "Genre_4"),
                new Genre(5, "Genre_5"),
                new Genre(6, "Genre_6"));

        Assertions.assertThat(actualGenres).usingRecursiveFieldByFieldElementComparator(configuration)
                .containsAll(expectedGenres);
    }

    @DisplayName("should return correct genres by id")
    @Test
    public void shouldReturnCorrectGenresById() {
        RecursiveComparisonConfiguration configuration = RecursiveComparisonConfiguration.builder()
                .build();

        List<Genre> actualGenres = jdbcGenreRepository.findAllByIds(genreIds);
        List<Genre> expectedGenres = List.of(new Genre(1, "Genre_1"),
                new Genre(2, "Genre_2"),
                new Genre(3, "Genre_3"));

        Assertions.assertThat(actualGenres).usingRecursiveFieldByFieldElementComparator(configuration)
                .containsAll(expectedGenres);
    }
}
