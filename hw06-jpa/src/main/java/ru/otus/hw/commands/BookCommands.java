package ru.otus.hw.commands;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import ru.otus.hw.converters.BookConverter;
import ru.otus.hw.converters.CommentConverter;
import ru.otus.hw.services.BookService;

import java.util.Set;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@ShellComponent
public class BookCommands {
    private final BookService bookService;

    private final BookConverter bookConverter;

    private final CommentConverter commentConverter;

    @Transactional
    @ShellMethod(value = "Find all books", key = "ab")
    public String findAllBooks() {
        return bookService.findAll().stream()
                .map(bookConverter::bookToString)
                .collect(Collectors.joining("," + System.lineSeparator()));
    }

    @Transactional
    @ShellMethod(value = "Find book by id", key = "bbid")
    public String findBookById(long id) {
        return bookService.findById(id)
                .map(bookConverter::bookToString)
                .orElse("Book with id %d not found".formatted(id));
    }

    @Transactional
    @ShellMethod(value = "Find comments by book id", key = "cbid")
    public String findCommentsByBookId(long id) {
        return bookService.findById(id)
                .map(b -> b.getComments().stream()
                        .map(commentConverter::commentToString)
                        .collect(Collectors.joining("," + System.lineSeparator())))
                .orElse("Comments for book %d not found".formatted(id));
    }

    @Transactional
    @ShellMethod(value = "Insert book", key = "bins")
    public String insertBook(String title, Set<Long> authorIds, Set<Long> genresIds) {
        var savedBook = bookService.insert(title, authorIds, genresIds);
        return bookConverter.bookToString(savedBook);
    }

    @Transactional
    @ShellMethod(value = "Update book", key = "bupd")
    public String updateBook(long id, String title, Set<Long> authorIds, Set<Long> genresIds) {
        var savedBook = bookService.update(id, title, authorIds, genresIds);
        return bookConverter.bookToString(savedBook);
    }

    @ShellMethod(value = "Delete book by id", key = "bdel")
    public void deleteBook(long id) {
        bookService.deleteById(id);
    }
}
