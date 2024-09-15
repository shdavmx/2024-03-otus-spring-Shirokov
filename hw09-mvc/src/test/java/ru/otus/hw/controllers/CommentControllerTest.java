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
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import ru.otus.hw.models.CommentFormModel;
import ru.otus.hw.services.CommentServiceImpl;

import java.util.List;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@DisplayName("Tests for CommentController")
@WebMvcTest({CommentController.class})
@TestPropertySource(properties = {"mongock.enabled=false"})
public class CommentControllerTest {
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

        mvc.perform(post("/comments/add")
                        .param("comment", expectedComment.getComment())
                        .param("bookId", expectedComment.getBookId()))
                .andExpect(status().is3xxRedirection());

        List<String> actualArgs = args.getAllValues();

        Assertions.assertThat(actualArgs.get(0)).isEqualTo(expectedComment.getComment());
        Assertions.assertThat(actualArgs.get(1)).isEqualTo(expectedComment.getBookId());
    }
}
