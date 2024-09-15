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
import ru.otus.hw.models.dto.GenreDto;
import ru.otus.hw.services.GenreServiceImpl;

import java.util.List;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@DisplayName("Tests for GenreController")
@WebMvcTest({GenreController.class})
@TestPropertySource(properties = {"mongock.enabled=false"})
public class GenreControllerTest {
    private final List<GenreDto> testGenres = List.of(
            new GenreDto("1", "Genre_1"),
            new GenreDto("2", "Genre_2")
    );

    private static final String TEST_GENRE_ID = "1";

    @Captor
    private ArgumentCaptor<String> args;

    @Autowired
    private MockMvc mvc;

    @Autowired
    private ObjectMapper mapper;

    @MockBean
    private GenreServiceImpl genreService;

    @DisplayName("should return all expected genres")
    @Test
    public void shouldReturnAllExpectedGenres() throws Exception {
        given(genreService.findAll()).willReturn(testGenres);

        mvc.perform(get("/genres"))
                .andExpect(status().isOk())
                .andExpect(view().name("genres"))
                .andExpect(model().attributeExists("genres"))
                .andExpect(model().attribute("genres", testGenres));
    }

    @DisplayName("should delete genre by id")
    @Test
    public void shouldDeleteGenreById() throws Exception {
        mvc.perform(get("/genres/delete?id=" + TEST_GENRE_ID))
                .andExpect(status().is3xxRedirection());
        verify(genreService, times(1)).deleteById(TEST_GENRE_ID);
    }

    @DisplayName("should return genre to edit")
    @Test
    public void shouldReturnGenreToEdit() throws Exception {
        given(genreService.findById(TEST_GENRE_ID)).willReturn(testGenres.get(0));

        mvc.perform(get("/genres/edit?id="+TEST_GENRE_ID))
                .andExpect(status().isOk())
                .andExpect(view().name("genre-edit"))
                .andExpect(model().attributeExists("genre"))
                .andExpect(model().attribute("genre", testGenres.get(0)));
    }

    @DisplayName("should save genre")
    @Test
    public void shouldSaveGenre() throws Exception {
        given(genreService.update(args.capture(), args.capture()))
                .willReturn(testGenres.get(0));

        mvc.perform(post("/genres/edit")
                        .param("id", testGenres.get(0).getId())
                        .param("name", testGenres.get(0).getName()))
                .andExpect(status().is3xxRedirection());

        List<String> actualArgs = args.getAllValues();

        Assertions.assertThat(actualArgs.get(0)).isEqualTo(testGenres.get(0).getId());
        Assertions.assertThat(actualArgs.get(1)).isEqualTo(testGenres.get(0).getName());
    }
}
