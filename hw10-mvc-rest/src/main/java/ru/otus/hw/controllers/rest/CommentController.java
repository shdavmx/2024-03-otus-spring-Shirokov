package ru.otus.hw.controllers.rest;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestBody;
import ru.otus.hw.models.CommentFormModel;
import ru.otus.hw.models.dto.CommentDto;
import ru.otus.hw.services.CommentService;

import java.util.List;

@RequiredArgsConstructor
@RestController
public class CommentController {
    private final CommentService commentService;

    @GetMapping("/api/comments")
    public ResponseEntity<List<CommentDto>> getCommentsByBookId(@RequestParam("bookId") String bookId) {
        return new ResponseEntity<>(commentService.findAllByBookId(bookId), HttpStatus.OK);
    }

    @PostMapping("/api/comments")
    public ResponseEntity<CommentFormModel> addComment(@RequestBody CommentFormModel comment) {
        if (comment == null || comment.getComment().isEmpty()) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        commentService.insert(comment.getComment(), comment.getBookId());
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
