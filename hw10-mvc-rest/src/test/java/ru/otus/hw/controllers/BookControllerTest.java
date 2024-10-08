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
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import ru.otus.hw.controllers.rest.BookController;
import ru.otus.hw.models.BookFormModel;
import ru.otus.hw.models.CommentFormModel;
import ru.otus.hw.models.dto.AuthorDto;
import ru.otus.hw.models.dto.BookDto;
import ru.otus.hw.models.dto.CommentDto;
import ru.otus.hw.models.dto.GenreDto;
import ru.otus.hw.services.*;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;

@DisplayName("Tests for BookController")
@WebMvcTest
@Import({
        BookController.class,
        AuthorServiceImpl.class,
        GenreServiceImpl.class,
        CommentServiceImpl.class
})
@TestPropertySource(properties = {"mongock.enabled=false"})
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

    @Captor
    private ArgumentCaptor<String> args;

    @Autowired
    private MockMvc mvc;

    @Autowired
    private ObjectMapper mapper;

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
    public void shouldReturnAllBooks() throws Exception {
        given(bookService.findAll()).willReturn(testBooks);

        mvc.perform(get("/api/books"))
                .andExpect(status().isOk())
                .andExpect(content().json(mapper.writeValueAsString(testBooks)));
    }

    @DisplayName("should delete book by id")
    @Test
    public void shouldDeleteBookById() throws Exception {
        mvc.perform(delete("/api/books?id=" + TEST_BOOK_ID))
                .andExpect(status().isOk());
        verify(bookService, times(1)).deleteById(TEST_BOOK_ID);
    }

    @DisplayName("should return book to edit")
    @Test
    public void shouldReturnBookToEdit() throws Exception {
        given(bookService.findById(TEST_BOOK_ID)).willReturn(testBooks.get(0));

        mvc.perform(get("/api/books/"+TEST_BOOK_ID))
                .andExpect(status().isOk())
                .andExpect(content().json(mapper.writeValueAsString(testBooks.get(0))));
    }

    @DisplayName("should save book")
    @Test
    public void shouldSaveBook() throws Exception {
        given(bookService.update(any(), any(), any(), any()))
                .willReturn(testBooks.get(0));

        mvc.perform(post("/api/books")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(testBooks.get(0).toFormModel())))
                .andExpect(status().isOk());
    }
}
