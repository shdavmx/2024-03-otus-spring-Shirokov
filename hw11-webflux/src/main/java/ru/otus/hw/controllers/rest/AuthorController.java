package ru.otus.hw.controllers.rest;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.otus.hw.models.dto.AuthorDto;
import ru.otus.hw.services.AuthorService;

@RequiredArgsConstructor
@RestController
public class AuthorController {
    private final AuthorService authorService;

    @GetMapping("/api/authors")
    public ResponseEntity<Flux<AuthorDto>> getAuthors() {
        return new ResponseEntity<>(authorService.findAll(), HttpStatus.OK);
    }

    @DeleteMapping("/api/authors/{id}")
    public ResponseEntity<Mono<Void>> deleteAuthorById(@PathVariable String id) {
        return new ResponseEntity<>(authorService.deleteById(id), HttpStatus.OK);
    }

    @GetMapping("/api/authors/{id}")
    public ResponseEntity<Mono<AuthorDto>> getEditAuthor(@PathVariable String id) {
        return new ResponseEntity<>(authorService.findById(id), HttpStatus.OK);
    }

    @PostMapping("/api/authors")
    public ResponseEntity<Mono<AuthorDto>> saveAuthor(@RequestBody AuthorDto author) {
        if (author == null || author.getFullName().isEmpty()) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        if (author.getId().equals("0")) {
            return new ResponseEntity<>(authorService.insert(author.getFullName()), HttpStatus.CREATED);
        }

        return new ResponseEntity<>(authorService.update(author.getId(), author.getFullName()), HttpStatus.OK);
    }
}
