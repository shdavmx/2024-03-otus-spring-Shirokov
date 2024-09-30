package hw.controllers;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.otus.hw.controllers.rest.GenreController;
import ru.otus.hw.models.dto.GenreDto;
import ru.otus.hw.services.GenreServiceImpl;

import java.util.Collections;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

@DisplayName("Tests for GenreController")
@SpringBootTest(classes = {GenreController.class},
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestPropertySource(properties = {"mongock.enabled=false"})
@EnableAutoConfiguration
public class GenreControllerTest {
    private final List<GenreDto> testGenres = List.of(
            new GenreDto("1", "Genre_1"),
            new GenreDto("2", "Genre_2")
    );

    private static final String TEST_GENRE_ID = "1";

    @Autowired
    private WebTestClient webTestClient;

    @MockBean
    private GenreServiceImpl genreService;

    @DisplayName("should return all expected genres")
    @Test
    public void shouldReturnAllExpectedGenres() {
        given(genreService.findAll()).willReturn(Flux.fromIterable(testGenres));

        WebTestClient.ResponseSpec response = webTestClient.get().uri("/api/genres")
                .accept(MediaType.APPLICATION_JSON)
                .exchange();

        response.expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBodyList(GenreDto.class);
    }

    @DisplayName("should delete genre by id")
    @Test
    public void shouldDeleteGenreById() {
        given(genreService.deleteById(TEST_GENRE_ID)).willReturn(Mono.empty());

        WebTestClient.ResponseSpec response = webTestClient.delete()
                .uri("/api/genres/{id}", Collections.singletonMap("id", TEST_GENRE_ID))
                .exchange();

        response.expectStatus().isOk();
    }

    @DisplayName("should return genre to edit")
    @Test
    public void shouldReturnGenreToEdit() throws Exception {
        given(genreService.findById(TEST_GENRE_ID))
                .willReturn(Mono.just(testGenres.get(0)));

        WebTestClient.ResponseSpec response = webTestClient.get()
                .uri("/api/genres/{id}", Collections.singletonMap("id", TEST_GENRE_ID))
                .exchange();

        response.expectStatus().isOk()
                .expectBody()
                .jsonPath("$.name").isEqualTo(testGenres.get(0).getName())
                .jsonPath("$.id").isEqualTo(testGenres.get(0).getId());
    }

    @DisplayName("should save genre")
    @Test
    public void shouldSaveGenre() throws Exception {
        given(genreService.update(any(), any()))
                .willReturn(Mono.just(testGenres.get(0)));

        WebTestClient.ResponseSpec response = webTestClient.post()
                .uri("/api/genres")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .body(Mono.just(testGenres.get(0)), GenreDto.class)
                .exchange();

        response.expectStatus().is2xxSuccessful()
                .expectBody()
                .jsonPath("$.name").isEqualTo(testGenres.get(0).getName())
                .jsonPath("$.id").isEqualTo(testGenres.get(0).getId());
    }
}
