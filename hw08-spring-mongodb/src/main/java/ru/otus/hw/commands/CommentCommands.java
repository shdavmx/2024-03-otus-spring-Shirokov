package ru.otus.hw.commands;

import lombok.RequiredArgsConstructor;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import ru.otus.hw.converters.CommentConverter;
import ru.otus.hw.services.CommentService;

import java.util.stream.Collectors;

@RequiredArgsConstructor
@ShellComponent
public class CommentCommands {
    private final CommentService commentService;

    private final CommentConverter commentConverter;

    @ShellMethod(value = "Find comment by id", key = "cid")
    public String findCommentById(String id) {
        return commentConverter.commentToString(commentService.findById(id));
    }

    @ShellMethod(value = "Find comments by book id", key = "cbid")
    public String findCommentsByBookId(String id) {
        return commentService.findAllByBookId(id).stream()
                .map(commentConverter::commentToString)
                .collect(Collectors.joining("," + System.lineSeparator()));
    }

    @ShellMethod(value = "Insert new comment", key = "cins")
    public String insertNewComment(String comment, String bookId) {
        return commentConverter.commentToString(commentService.insert(comment, bookId));
    }

    @ShellMethod(value = "Update comment", key = "cupd")
    public String updateComment(String id, String comment, String bookId) {
        return commentConverter.commentToString(commentService.update(id, comment, bookId));
    }

    @ShellMethod(value = "Delete comment", key = "cdel")
    public void deleteComment(String id) {
        commentService.deleteById(id);
    }
}
