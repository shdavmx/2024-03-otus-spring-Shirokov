package ru.otus.hw.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Document(collection = "comments")
public class Comment {
    @Id
    private String id;

    private String comment;

    @DBRef(lazy = true)
    private Book book;

    public Comment(String comment) {
        this.comment = comment;
    }
}
