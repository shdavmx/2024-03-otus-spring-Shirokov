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
    public Mono<String> getAuthorView() {
        return Mono.just("authors");
    }

    @GetMapping("/genres")
    public Mono<String> getGenreView() {
        return Mono.just("genres");
    }

    @GetMapping("/books")
    public Mono<String> getBooksView() {
        return Mono.just("books");
    }
}
