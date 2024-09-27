package ru.otus.hw.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import reactor.core.publisher.Mono;


@Controller
public class LibraryPageController {

    @GetMapping("/")
    public Mono<String> getLibraryView() {
        return Mono.just("library");
    }

    @GetMapping("/authors")
    public String getAuthorView() {
        return "authors";
    }

    @GetMapping("/genres")
    public String getGenreView() {
        return "genres";
    }

    @GetMapping("/books")
    public String getBooksView() {
        return "books";
    }
}
