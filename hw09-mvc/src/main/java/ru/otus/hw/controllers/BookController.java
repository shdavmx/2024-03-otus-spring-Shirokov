package ru.otus.hw.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.otus.hw.models.dto.AuthorDto;
import ru.otus.hw.models.dto.BookDto;
import ru.otus.hw.models.dto.GenreDto;
import ru.otus.hw.services.AuthorService;
import ru.otus.hw.services.BookService;
import ru.otus.hw.services.GenreService;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Controller
public class BookController {
    private final BookService bookService;

    private final AuthorService authorService;

    private final GenreService genreService;

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

    @GetMapping("/books/edit")
    public String getEditBook(@RequestParam("id") String id, Model model) {
        List<AuthorDto> authors = authorService.findAll();
        List<GenreDto> genres = genreService.findAll();
        BookDto book = bookService.findById(id);
        model.addAttribute("book", book);
        model.addAttribute("authors", authors);
        model.addAttribute("genres", genres);
        return "book-edit";
    }

    @PostMapping("/books/edit")
    public String saveAuthor(BookDto book, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "book-edit";
        }

        bookService.update(book.getId(), book.getTitle(),
                book.getAuthor().getId(), book.getGenres().stream().map(GenreDto::getId).collect(Collectors.toSet()));
        return "redirect:/books";
    }
}
