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
import ru.otus.hw.controllers.rest.GenreController;
import ru.otus.hw.models.dto.GenreDto;
import ru.otus.hw.services.GenreServiceImpl;

import java.util.List;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
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

        mvc.perform(get("/api/genres"))
                .andExpect(status().isOk())
                .andExpect(content().json(mapper.writeValueAsString(testGenres)));
    }

    @DisplayName("should delete genre by id")
    @Test
    public void shouldDeleteGenreById() throws Exception {
        mvc.perform(delete("/api/genres?id=" + TEST_GENRE_ID))
                .andExpect(status().isOk());
        verify(genreService, times(1)).deleteById(TEST_GENRE_ID);
    }

    @DisplayName("should return genre to edit")
    @Test
    public void shouldReturnGenreToEdit() throws Exception {
        given(genreService.findById(TEST_GENRE_ID)).willReturn(testGenres.get(0));

        mvc.perform(get("/api/genres/"+TEST_GENRE_ID))
                .andExpect(status().isOk())
                .andExpect(content().json(mapper.writeValueAsString(testGenres.get(0))));
    }

    @DisplayName("should save genre")
    @Test
    public void shouldSaveGenre() throws Exception {
        given(genreService.update(args.capture(), args.capture()))
                .willReturn(testGenres.get(0));

        mvc.perform(post("/api/genres")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(testGenres.get(0))))
                .andExpect(status().isOk());

        List<String> actualArgs = args.getAllValues();

        Assertions.assertThat(actualArgs.get(0)).isEqualTo(testGenres.get(0).getId());
        Assertions.assertThat(actualArgs.get(1)).isEqualTo(testGenres.get(0).getName());
    }
}
