package ru.otus.hw.events.listeners;

import lombok.RequiredArgsConstructor;
import org.bson.Document;
import org.springframework.data.mongodb.core.mapping.event.AbstractMongoEventListener;
import org.springframework.data.mongodb.core.mapping.event.BeforeDeleteEvent;
import org.springframework.stereotype.Component;
import ru.otus.hw.models.Author;
import ru.otus.hw.models.Book;
import ru.otus.hw.repositories.BookRepository;

import java.util.List;

@RequiredArgsConstructor
@Component
public class AuthorCascadeDeleteMongoEventListener extends AbstractMongoEventListener<Author> {
    private final BookRepository bookRepository;

    @Override
    public void onBeforeDelete(BeforeDeleteEvent<Author> event) {
        Document source = event.getSource();
        List<Book> books = bookRepository.findAllByAuthorId(source.getString("_id"));
        if (!books.isEmpty()) {
            bookRepository.deleteAllById(books.stream().map(Book::getId).toList());
        }
    }
}
