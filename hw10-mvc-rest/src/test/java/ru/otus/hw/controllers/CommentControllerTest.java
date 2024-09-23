package ru.otus.hw.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import ru.otus.hw.controllers.rest.CommentController;
import ru.otus.hw.models.CommentFormModel;
import ru.otus.hw.models.dto.AuthorDto;
import ru.otus.hw.models.dto.BookDto;
import ru.otus.hw.models.dto.CommentDto;
import ru.otus.hw.models.dto.GenreDto;
import ru.otus.hw.services.CommentServiceImpl;

import java.util.List;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@DisplayName("Tests for CommentController")
@WebMvcTest({CommentController.class})
@TestPropertySource(properties = {"mongock.enabled=false"})
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

    @Captor
    private ArgumentCaptor<String> args;

    @Autowired
    private MockMvc mvc;

    @Autowired
    private ObjectMapper mapper;

    @MockBean
    private CommentServiceImpl commentService;

    @DisplayName("should add new comment")
    @Test
    public void shouldAddNewComment() throws Exception {
        CommentFormModel expectedComment = new CommentFormModel("comment", "1");

        given(commentService.insert(args.capture(), args.capture()))
                .willReturn(null);

        mvc.perform(post("/api/comments")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(expectedComment)))
                .andExpect(status().isOk());

        List<String> actualArgs = args.getAllValues();

        Assertions.assertThat(actualArgs.get(0)).isEqualTo(expectedComment.getComment());
        Assertions.assertThat(actualArgs.get(1)).isEqualTo(expectedComment.getBookId());
    }

    @DisplayName("should return expected comments by book id")
    @Test
    public void shouldReturnExpectedCommentsByBookId() throws Exception {
        given(commentService.findAllByBookId(TEST_BOOK_ID)).willReturn(testComments);

        mvc.perform(get("/api/comments?bookId=" + TEST_BOOK_ID))
                .andExpect(status().isOk())
                .andExpect(content().json(mapper.writeValueAsString(testComments)));
    }
}
