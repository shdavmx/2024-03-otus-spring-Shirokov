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
import ru.otus.hw.models.dto.AuthorDto;
import ru.otus.hw.services.AuthorService;

import java.util.List;

@RequiredArgsConstructor
@RestController
public class AuthorController {
    private final AuthorService authorService;

    @GetMapping("/api/authors")
    public ResponseEntity<List<AuthorDto>> getAuthors() {
        return new ResponseEntity<>(authorService.findAll(), HttpStatus.OK);
    }

    @DeleteMapping("/api/authors")
    public ResponseEntity<String> deleteAuthorById(@RequestParam String id) {
        authorService.deleteById(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/api/authors/{id}")
    public ResponseEntity<AuthorDto> getEditAuthor(@PathVariable String id) {
        return new ResponseEntity<>(authorService.findById(id), HttpStatus.OK);
    }

    @PostMapping("/api/authors")
    public ResponseEntity<AuthorDto> saveAuthor(@RequestBody AuthorDto author) {
        if (author == null || author.getFullName().isEmpty()) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        if (author.getId().equals("0")) {
            authorService.insert(author.getFullName());
        } else {
            authorService.update(author.getId(), author.getFullName());
        }

        return new ResponseEntity<>(HttpStatus.OK);
    }
}
