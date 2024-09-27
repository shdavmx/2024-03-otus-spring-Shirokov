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
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.otus.hw.models.dto.GenreDto;
import ru.otus.hw.services.GenreService;

import java.util.List;

@RequiredArgsConstructor
@RestController
public class GenreController {
    private final GenreService genreService;

    @GetMapping("/api/genres")
    public ResponseEntity<Flux<GenreDto>> getGenres() {
        return new ResponseEntity<>(genreService.findAll(), HttpStatus.OK);
    }

    @DeleteMapping("/api/genres")
    public ResponseEntity<Mono<Void>> deleteGenreById(@RequestParam("id") String id) {
        return new ResponseEntity<>(genreService.deleteById(id), HttpStatus.OK);
    }

    @GetMapping("/api/genres/{id}")
    public ResponseEntity<Mono<GenreDto>> getEditGenre(@PathVariable String id) {
        return new ResponseEntity<>(genreService.findById(id), HttpStatus.OK);
    }

    @PostMapping("/api/genres")
    public ResponseEntity<Mono<GenreDto>> saveGenre(@RequestBody GenreDto genre) {
        if (genre == null || genre.getName().isEmpty()) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        if (genre.getId().equals("0")) {
            return new ResponseEntity<>(genreService.insert(genre.getName()), HttpStatus.CREATED);
        }

        return new ResponseEntity<>(genreService.update(genre.getId(), genre.getName()), HttpStatus.OK);
    }
}
