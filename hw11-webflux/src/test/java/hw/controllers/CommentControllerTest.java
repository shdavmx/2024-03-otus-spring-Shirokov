package hw.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.test.web.servlet.MockMvc;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.otus.hw.controllers.rest.CommentController;
import ru.otus.hw.models.CommentFormModel;
import ru.otus.hw.models.dto.AuthorDto;
import ru.otus.hw.models.dto.BookDto;
import ru.otus.hw.models.dto.CommentDto;
import ru.otus.hw.models.dto.GenreDto;
import ru.otus.hw.services.CommentServiceImpl;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@DisplayName("Tests for CommentController")
@SpringBootTest(classes = {CommentController.class},
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestPropertySource(properties = {"mongock.enabled=false"})
@EnableAutoConfiguration
public class CommentControllerTest {
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

    @DisplayName("should add new comment")
    @Test
    public void shouldAddNewComment() {
        CommentFormModel expectedComment = new CommentFormModel("Comment_1", "1");

        given(commentService.insert(any(), any()))
                .willReturn(Mono.just(testComments.get(0)));

        WebTestClient.ResponseSpec response = webTestClient.post()
                .uri("/api/comments")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .body(Mono.just(expectedComment), CommentFormModel.class)
                .exchange();

        response.expectStatus().is2xxSuccessful()
                .expectBody()
                .jsonPath("$.comment").isEqualTo(expectedComment.getComment())
                .jsonPath("$.book.id").isEqualTo(expectedComment.getBookId());
    }

    @DisplayName("should return expected comments by book id")
    @Test
    public void shouldReturnExpectedCommentsByBookId() {
        given(commentService.findAllByBookId(TEST_BOOK_ID))
                .willReturn(Flux.fromIterable(testComments));

        WebTestClient.ResponseSpec response = webTestClient.get().uri("/api/comments?bookId="+TEST_BOOK_ID)
                .accept(MediaType.APPLICATION_JSON)
                .exchange();

        response.expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBodyList(CommentDto.class);
    }
}
