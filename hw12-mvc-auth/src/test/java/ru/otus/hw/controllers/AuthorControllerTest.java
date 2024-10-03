package ru.otus.hw.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.mockito.BDDMockito.given;

import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import ru.otus.hw.models.dto.AuthorDto;
import ru.otus.hw.security.configs.SecurityConfig;
import ru.otus.hw.services.AuthorService;
import org.springframework.security.test.context.support.*;

import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@DisplayName("Tests for AuthorController")
@WebMvcTest({AuthorController.class})
@TestPropertySource(properties = {"mongock.enabled=false"})
@Import(SecurityConfig.class)
public class AuthorControllerTest {
    private static final String TEST_AUTHOR_ID = "1";

    private final List<AuthorDto> testAuthors = List.of(
            new AuthorDto("1", "Author_1"),
            new AuthorDto("2", "Author_2")
    );

    @Captor
    private ArgumentCaptor<String> args;

    @Autowired
    private MockMvc mvc;

    @Autowired
    private ObjectMapper mapper;

    @MockBean
    private AuthorService authorService;

    @WithMockUser(
            username = "admin",
            authorities = {"ROLE_LIBROLE"}
    )
    @DisplayName("should return all expected authors")
    @Test
    public void shouldReturnAllExpectedAuthors() throws Exception {
        given(authorService.findAll()).willReturn(testAuthors);

        mvc.perform(get("/authors"))
                .andExpect(status().isOk())
                .andExpect(view().name("authors"))
                .andExpect(model().attributeExists("authors"))
                .andExpect(model().attribute("authors", testAuthors));
    }

    @DisplayName("should return 302 for get all authors")
    @Test
    public void shouldReturn302ForGetAllAuthors() throws Exception {
        given(authorService.findAll()).willReturn(testAuthors);

        mvc.perform(get("/authors"))
                .andExpect(status().isFound());
    }

    @WithMockUser(
            username = "admin",
            authorities = {"ROLE_LIBROLE"}
    )
    @DisplayName("should delete author by id")
    @Test
    public void shouldDeleteAuthorById() throws Exception {
        mvc.perform(post("/authors/delete?id=" + TEST_AUTHOR_ID))
                .andExpect(status().is3xxRedirection());
        verify(authorService, times(1)).deleteById(TEST_AUTHOR_ID);
    }

    @DisplayName("should return 302 for delete author by id")
    @Test
    public void shouldReturn302ForDeleteAuthorById() throws Exception {
        mvc.perform(post("/authors/delete?id=" + TEST_AUTHOR_ID))
                .andExpect(status().isFound());
    }

    @WithMockUser(
            username = "admin",
            authorities = {"ROLE_LIBROLE"}
    )
    @DisplayName("should return author to edit")
    @Test
    public void shouldReturnAuthorToEdit() throws Exception {
        given(authorService.findById(TEST_AUTHOR_ID)).willReturn(testAuthors.get(0));

        mvc.perform(get("/authors/edit/"+TEST_AUTHOR_ID))
                .andExpect(status().isOk())
                .andExpect(view().name("author-edit"))
                .andExpect(model().attributeExists("author"))
                .andExpect(model().attribute("author", testAuthors.get(0)));
    }

    @DisplayName("should return 302 for get author to edit")
    @Test
    public void shouldReturn302ForGetAuthorToEdit() throws Exception {
        mvc.perform(get("/authors/edit/"+TEST_AUTHOR_ID))
                .andExpect(status().isFound());
    }

    @WithMockUser(
            username = "admin",
            authorities = {"ROLE_LIBROLE"}
    )
    @DisplayName("should save author")
    @Test
    public void shouldSaveAuthor() throws Exception {
        given(authorService.update(args.capture(), args.capture()))
                .willReturn(testAuthors.get(0));

        mvc.perform(post("/authors/edit")
                        .param("id", testAuthors.get(0).getId())
                        .param("fullName", testAuthors.get(0).getFullName()))
                .andExpect(status().is3xxRedirection());

        List<String> actualArgs = args.getAllValues();

        Assertions.assertThat(actualArgs.get(0)).isEqualTo(testAuthors.get(0).getId());
        Assertions.assertThat(actualArgs.get(1)).isEqualTo(testAuthors.get(0).getFullName());
    }

    @DisplayName("should return 302 for save author")
    @Test
    public void shouldReturn302ForSaveAuthor() throws Exception {
        mvc.perform(post("/authors/edit")
                        .param("id", testAuthors.get(0).getId())
                        .param("fullName", testAuthors.get(0).getFullName()))
                .andExpect(status().isFound());
    }
}
