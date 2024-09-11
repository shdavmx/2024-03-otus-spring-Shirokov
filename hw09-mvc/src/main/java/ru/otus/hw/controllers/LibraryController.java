package ru.otus.hw.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;


@Controller
public class LibraryController {
    @GetMapping("/")
    public String getLibraryView() {
        return "library";
    }
}
