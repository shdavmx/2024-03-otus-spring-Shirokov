package ru.otus.hw.controllers;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.otus.hw.models.BookFormModel;
import ru.otus.hw.models.CommentFormModel;
import ru.otus.hw.models.dto.AuthorDto;
import ru.otus.hw.models.dto.BookDto;
import ru.otus.hw.models.dto.CommentDto;
import ru.otus.hw.models.dto.GenreDto;
import ru.otus.hw.services.AuthorService;
import ru.otus.hw.services.BookService;
import ru.otus.hw.services.CommentService;
import ru.otus.hw.services.GenreService;

import java.util.List;
import java.util.Objects;

@RequiredArgsConstructor
@Controller
public class BookController {
    private final BookService bookService;

    private final AuthorService authorService;

    private final GenreService genreService;

    private final CommentService commentService;

    @GetMapping("/books")
    public String bookList(Model model) {
        List<BookDto> books = bookService.findAll();
        model.addAttribute("books", books);
        return "books";
    }

    @GetMapping("/books/delete")
    public String deleteBookById(@RequestParam("id") String id) {
        bookService.deleteById(id);
        return "redirect:/books";
    }

    @GetMapping("/books/info")
    public String getInfoBook(@RequestParam("id") String id, Model model) {
        BookDto book = bookService.findById(id);
        List<CommentDto> comments = commentService.findAllByBookId(id);
        model.addAttribute("book", book);
        model.addAttribute("comment", new CommentFormModel("", book.getId()));
        model.addAttribute("comments", comments);
        return "book-details";
    }

    @GetMapping("/books/edit")
    public String getEditBook(@RequestParam("id") String id, Model model) {
        List<AuthorDto> authors = authorService.findAll();
        List<GenreDto> genres = genreService.findAll();
        BookDto book = new BookDto("0", "New_Book", authors.get(0), List.of(genres.get(0)));
        if (!Objects.equals(id, "0")) {
            book = bookService.findById(id);
        }
        model.addAttribute("book", book.toFormModel());
        model.addAttribute("authors", authors);
        model.addAttribute("genres", genres);
        return "book-edit";
    }

    @PostMapping("/books/edit")
    public String saveAuthor(@Valid BookFormModel book, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "book-edit";
        }

        if (book.getId().equals("0")) {
            bookService.insert(book.getTitle(), book.getAuthorId(), book.getGenreIds());
        } else {
            bookService.update(book.getId(), book.getTitle(), book.getAuthorId(), book.getGenreIds());
        }

        return "redirect:/books";
    }
}
