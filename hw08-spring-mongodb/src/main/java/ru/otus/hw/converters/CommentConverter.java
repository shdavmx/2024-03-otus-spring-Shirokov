package ru.otus.hw.converters;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.otus.hw.models.Book;
import ru.otus.hw.models.Comment;
import ru.otus.hw.models.dto.BookDto;
import ru.otus.hw.models.dto.CommentDto;

@RequiredArgsConstructor
@Component
public class CommentConverter {
    private final BookConverter bookConverter;

    public String commentToString(CommentDto comment) {
        return "Id: %s, Comment: %s".formatted(
                comment.getId(),
                comment.getComment());
    }

    public CommentDto toDto(Comment comment) {
        BookDto book = bookConverter.toDto(comment.getBook());
        return new CommentDto(comment.getId(), comment.getComment(), book);
    }

    public Comment toDbEntry(CommentDto comment) {
        Book book = bookConverter.toDbEntry(comment.getBook());
        return new Comment(comment.getId(), comment.getComment(), book);
    }
}
