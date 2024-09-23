package ru.otus.hw.controllers.rest;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import ru.otus.hw.models.dto.GenreDto;
import ru.otus.hw.services.GenreService;

import java.util.List;

@RequiredArgsConstructor
@RestController
public class GenreController {
    private final GenreService genreService;

    @GetMapping("/api/genres")
    public ResponseEntity<List<GenreDto>> getGenres() {
        return new ResponseEntity<>(genreService.findAll(), HttpStatus.OK);
    }

    @DeleteMapping("/api/genres")
    public ResponseEntity<String> deleteGenreById(@RequestParam("id") String id) {
        genreService.deleteById(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/api/genres/{id}")
    public ResponseEntity<GenreDto> getEditGenre(@PathVariable String id) {
        return new ResponseEntity<>(genreService.findById(id), HttpStatus.OK);
    }

    @PostMapping("/api/genres")
    public ResponseEntity<GenreDto> saveGenre(@RequestBody GenreDto genre) {
        if (genre == null || genre.getName().isEmpty()) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        if (genre.getId().equals("0")) {
            genreService.insert(genre.getName());
        } else {
            genreService.update(genre.getId(), genre.getName());
        }

        return new ResponseEntity<>(HttpStatus.OK);
    }
}
