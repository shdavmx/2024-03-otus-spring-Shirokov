package ru.otus.hw.models.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.otus.hw.models.Comment;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class CommentDto {
    private String id;

    private String comment;

    private BookDto book;

    public CommentDto(Comment comment) {
        this.id = comment.getId();
        this.comment = comment.getComment();
        this.book = new BookDto(comment.getBook());
    }

    @Override
    public String toString() {
        return "Id: %s, Comment: %s".formatted(id, comment);
    }

    public Comment toDomainObject() {
        return new Comment(id, comment, book.toDomainObject());
    }
}
