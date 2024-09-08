package ru.otus.hw.events.listeners;

import lombok.RequiredArgsConstructor;
import org.bson.Document;
import org.springframework.data.mongodb.core.mapping.event.AbstractMongoEventListener;
import org.springframework.data.mongodb.core.mapping.event.BeforeDeleteEvent;
import org.springframework.stereotype.Component;
import ru.otus.hw.models.Book;
import ru.otus.hw.models.Comment;
import ru.otus.hw.repositories.CommentRepository;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Component
public class BookCascadeDeleteMongoEventListener extends AbstractMongoEventListener<Book> {
    private final CommentRepository commentRepository;

    @Override
    public void onBeforeDelete(BeforeDeleteEvent<Book> event) {
        Document source = event.getSource();
        Object valType = source.get("_id");
        List<String> ids = new ArrayList<>();

        if (valType instanceof String) {
            ids.add(valType.toString());
        } else {
            ids = source.get("_id", Document.class).getList("$in", String.class);
        }

        for (String id : ids) {
            List<Comment> comments = commentRepository.findAllByBookId(id);
            if (!comments.isEmpty()) {
                commentRepository.deleteAllById(comments.stream().map(Comment::getId).toList());
            }
        }
    }
}
