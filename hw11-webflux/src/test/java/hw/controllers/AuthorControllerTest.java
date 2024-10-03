package hw.controllers;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.otus.hw.controllers.rest.AuthorController;
import ru.otus.hw.models.dto.AuthorDto;
import ru.otus.hw.services.AuthorServiceImpl;

import java.util.Collections;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

@DisplayName("Tests for AuthorController")
@SpringBootTest(classes = {
        AuthorController.class,
}, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestPropertySource(properties = {"mongock.enabled=false"})
@EnableAutoConfiguration
public class AuthorControllerTest {
    private static final String TEST_AUTHOR_ID = "1";

    private final List<AuthorDto> testAuthors = List.of(
            new AuthorDto("1", "Author_1"),
            new AuthorDto("2", "Author_2")
    );

    @Autowired
    private WebTestClient webTestClient;

    @MockBean
    private AuthorServiceImpl authorService;

    @DisplayName("should return all expected authors")
    @Test
    public void shouldReturnAllExpectedAuthors() {
        given(authorService.findAll()).willReturn(Flux.fromIterable(testAuthors));

        WebTestClient.ResponseSpec response = webTestClient.get().uri("/api/authors")
                .accept(MediaType.APPLICATION_JSON)
                .exchange();

        response.expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBodyList(AuthorDto.class);
    }

    @DisplayName("should delete author by id")
    @Test
    public void shouldDeleteAuthorById() {
        given(authorService.deleteById(TEST_AUTHOR_ID)).willReturn(Mono.empty());

        WebTestClient.ResponseSpec response = webTestClient.delete()
                .uri("/api/authors/{id}", Collections.singletonMap("id", TEST_AUTHOR_ID))
                .exchange();

        response.expectStatus().isOk();
    }

    @DisplayName("should return author to edit")
    @Test
    public void shouldReturnAuthorToEdit() {
        given(authorService.findById(TEST_AUTHOR_ID))
                .willReturn(Mono.just(testAuthors.get(0)));

        WebTestClient.ResponseSpec response = webTestClient.get()
                .uri("/api/authors/{id}", Collections.singletonMap("id", TEST_AUTHOR_ID))
                .exchange();

        response.expectStatus().isOk()
                .expectBody()
                .jsonPath("$.fullName").isEqualTo(testAuthors.get(0).getFullName())
                .jsonPath("$.id").isEqualTo(testAuthors.get(0).getId());
    }

    @DisplayName("should save author")
    @Test
    public void shouldSaveAuthor() {
        given(authorService.update(any(), any()))
                .willReturn(Mono.just(testAuthors.get(0)));

        WebTestClient.ResponseSpec response = webTestClient.post()
                .uri("/api/authors")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .body(Mono.just(testAuthors.get(0)), AuthorDto.class)
                .exchange();

        response.expectStatus().is2xxSuccessful()
                .expectBody()
                .jsonPath("$.fullName").isEqualTo(testAuthors.get(0).getFullName())
                .jsonPath("$.id").isEqualTo(testAuthors.get(0).getId());
    }
}
