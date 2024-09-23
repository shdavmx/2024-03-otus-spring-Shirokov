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

    private final AuthorService authorService;

    private final GenreService genreService;

    private final CommentService commentService;

    @GetMapping("/api/books")
    public ResponseEntity<List<BookDto>> bookList() {
        return new ResponseEntity<>(bookService.findAll(), HttpStatus.OK);
    }

    @DeleteMapping("/api/books")
    public ResponseEntity<String> deleteBookById(@RequestParam("id") String id) {
        bookService.deleteById(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/api/books/{id}")
    public ResponseEntity<BookDto> getEditBook(@PathVariable String id, Model model) {
        return new ResponseEntity<>(bookService.findById(id), HttpStatus.OK);
    }

    @PostMapping("/api/books")
    public ResponseEntity<BookFormModel> saveAuthor(@RequestBody BookFormModel book) {
        if (book == null || book.getTitle().isEmpty() ||
            book.getAuthorId().isEmpty()) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        if (book.getId().equals("0")) {
            bookService.insert(book.getTitle(), book.getAuthorId(), book.getGenreIds());
        } else {
            bookService.update(book.getId(), book.getTitle(), book.getAuthorId(), book.getGenreIds());
        }

        return new ResponseEntity<>(HttpStatus.OK);
    }
}
