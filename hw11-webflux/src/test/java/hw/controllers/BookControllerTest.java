package hw.controllers;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.otus.hw.controllers.rest.BookController;
import ru.otus.hw.models.BookFormModel;
import ru.otus.hw.models.dto.AuthorDto;
import ru.otus.hw.models.dto.BookDto;
import ru.otus.hw.models.dto.CommentDto;
import ru.otus.hw.models.dto.GenreDto;
import ru.otus.hw.services.AuthorServiceImpl;
import ru.otus.hw.services.BookServiceImpl;
import ru.otus.hw.services.CommentServiceImpl;
import ru.otus.hw.services.GenreServiceImpl;

import java.util.Collections;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

@DisplayName("Tests for BookController")
@SpringBootTest(classes = BookController.class,
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Import({
        AuthorServiceImpl.class,
        GenreServiceImpl.class,
        CommentServiceImpl.class
})
@TestPropertySource(properties = {"mongock.enabled=false"})
@EnableAutoConfiguration
public class BookControllerTest {
    private static final String TEST_BOOK_ID = "1";

    private final List<AuthorDto> testAuthors = List.of(
            new AuthorDto("1", "Author_1"),
            new AuthorDto("2", "Author_2")
    );

    private final List<GenreDto> testGenres = List.of(
            new GenreDto("1", "Genre_1"),
            new GenreDto("2", "Genre_2")
    );

    private final List<BookDto> testBooks = List.of(
            new BookDto("1", "Title_1",
                    testAuthors.get(0),
                    testGenres),
            new BookDto("2", "Title_2",
                    testAuthors.get(1),
                    testGenres)
    );

    private final List<CommentDto> testComments = List.of(
            new CommentDto("1", "Comment_1", testBooks.get(0)),
            new CommentDto("2", "Comment_2", testBooks.get(0))
    );

    @Autowired
    private WebTestClient webTestClient;

    @MockBean
    private CommentServiceImpl commentService;

    @MockBean
    private GenreServiceImpl genreService;

    @MockBean
    private AuthorServiceImpl authorService;

    @MockBean
    private BookServiceImpl bookService;

    @DisplayName("should return all books")
    @Test
    public void shouldReturnAllBooks() {
        given(bookService.findAll()).willReturn(Flux.fromIterable(testBooks));

        WebTestClient.ResponseSpec response = webTestClient.get().uri("/api/books")
                .accept(MediaType.APPLICATION_JSON)
                .exchange();

        response.expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBodyList(BookDto.class);
    }

    @DisplayName("should delete book by id")
    @Test
    public void shouldDeleteBookById() throws Exception {
        given(bookService.deleteById(TEST_BOOK_ID)).willReturn(Mono.empty());

        WebTestClient.ResponseSpec response = webTestClient.delete()
                .uri("/api/books/{id}", Collections.singletonMap("id", TEST_BOOK_ID))
                .exchange();

        response.expectStatus().isOk();
    }

    @DisplayName("should return book to edit")
    @Test
    public void shouldReturnBookToEdit() throws Exception {
        given(bookService.findById(TEST_BOOK_ID)).willReturn(Mono.just(testBooks.get(0)));

        WebTestClient.ResponseSpec response = webTestClient.get()
                .uri("/api/books/{id}", Collections.singletonMap("id", TEST_BOOK_ID))
                .exchange();

        response.expectStatus().isOk()
                .expectBody()
                .jsonPath("$.title").isEqualTo(testBooks.get(0).getTitle())
                .jsonPath("$.id").isEqualTo(testBooks.get(0).getId());
    }

    @DisplayName("should save book")
    @Test
    public void shouldSaveBook() throws Exception {
        BookFormModel testBookModel = new BookFormModel(TEST_BOOK_ID,
                testBooks.get(0).getTitle(), testBooks.get(0).getAuthor().getId(),
                testBooks.get(0).getGenres().stream().map(GenreDto::getId).toList());
        given(bookService.update(any(), any(), any(), any()))
                .willReturn(Mono.just(testBooks.get(0)));

        WebTestClient.ResponseSpec response = webTestClient.post()
                .uri("/api/books")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .body(Mono.just(testBookModel), BookFormModel.class)
                .exchange();

        response.expectStatus().is2xxSuccessful()
                .expectBody()
                .jsonPath("$.title").isEqualTo(testBooks.get(0).getTitle())
                .jsonPath("$.id").isEqualTo(testBooks.get(0).getId());
    }
}
