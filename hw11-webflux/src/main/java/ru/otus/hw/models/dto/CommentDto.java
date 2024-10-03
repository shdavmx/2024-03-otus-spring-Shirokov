package ru.otus.hw.models.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import ru.otus.hw.models.Comment;

@AllArgsConstructor
@Data
public class CommentDto {
    private String id;

    private String comment;

    private BookDto book;

    @Override
    public String toString() {
        return "Id: %s, Comment: %s".formatted(id, comment);
    }

    public Comment toDomainObject() {
        return new Comment(id, comment, book.toDomainObject());
    }

    public static CommentDto fromDomainObject(Comment comment) {
        return new CommentDto(comment.getId(), comment.getComment(),
                BookDto.fromDomainObject(comment.getBook()));
    }
}
