package ru.otus.hw.controllers;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import ru.otus.hw.models.CommentFormModel;
import ru.otus.hw.services.CommentService;

@RequiredArgsConstructor
@Controller
public class CommentController {
    private final CommentService commentService;

    @PostMapping("comments/add")
    public String addComment(@Valid CommentFormModel comment,
                             BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            return "book-details";
        }

        commentService.insert(comment.getComment(), comment.getBookId());
        return "redirect:/books/info?id=" + comment.getBookId();
    }
}
