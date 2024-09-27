package ru.otus.hw.controllers.rest;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.otus.hw.models.BookFormModel;
import ru.otus.hw.models.dto.BookDto;
import ru.otus.hw.services.AuthorService;
import ru.otus.hw.services.BookService;
import ru.otus.hw.services.CommentService;
import ru.otus.hw.services.GenreService;

import java.util.List;

@RequiredArgsConstructor
@RestController
public class BookController {
    private final BookService bookService;

    @GetMapping("/api/books")
    public ResponseEntity<Flux<BookDto>> bookList() {
        return new ResponseEntity<>(bookService.findAll(), HttpStatus.OK);
    }

    @DeleteMapping("/api/books")
    public ResponseEntity<Mono<Void>> deleteBookById(@RequestParam("id") String id) {
        return new ResponseEntity<>(bookService.deleteById(id), HttpStatus.OK);
    }

    @GetMapping("/api/books/{id}")
    public ResponseEntity<Mono<BookDto>> getEditBook(@PathVariable String id, Model model) {
        return new ResponseEntity<>(bookService.findById(id), HttpStatus.OK);
    }

    @PostMapping("/api/books")
    public ResponseEntity<Mono<BookDto>> saveAuthor(@RequestBody BookFormModel book) {
        if (book == null || book.getTitle().isEmpty() ||
            book.getAuthorId().isEmpty()) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        if (book.getId().equals("0")) {
            return new ResponseEntity<>(bookService.insert(book.getTitle(), book.getAuthorId(), book.getGenreIds()),
                    HttpStatus.CREATED);
        }

        return new ResponseEntity<>(bookService.update(book.getId(), book.getTitle(), book.getAuthorId(), book.getGenreIds()),
                HttpStatus.OK);
    }
}
