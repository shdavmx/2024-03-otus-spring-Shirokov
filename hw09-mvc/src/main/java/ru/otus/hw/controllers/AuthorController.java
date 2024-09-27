package ru.otus.hw.controllers;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.otus.hw.models.dto.AuthorDto;
import ru.otus.hw.services.AuthorService;

import java.util.List;

@RequiredArgsConstructor
@Controller
public class AuthorController {
    private final AuthorService authorService;

    @GetMapping("/authors")
    public String getAuthors(Model model) {
        List<AuthorDto> authors = authorService.findAll();
        model.addAttribute("authors", authors);
        return "authors";
    }

    @PostMapping("/authors/delete")
    public String deleteAuthorById(@RequestParam("id") String id) {
        authorService.deleteById(id);
        return "redirect:/authors";
    }

    @GetMapping("/authors/edit/{id}")
    public String getEditAuthor(@PathVariable("id") String id, Model model) {
        AuthorDto author = new AuthorDto("0", "New author");
        if (!id.equals("0")) {
            author = authorService.findById(id);
        }
        model.addAttribute("author", author);
        return "author-edit";
    }

    @PostMapping("/authors/edit")
    public String saveAuthor(@Valid AuthorDto author, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "author-edit";
        }

        if (author.getId().equals("0")) {
            authorService.insert(author.getFullName());
        } else {
            authorService.update(author.getId(), author.getFullName());
        }

        return "redirect:/authors";
    }
}
