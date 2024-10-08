package ru.otus.hw.repositories;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.otus.hw.models.Book;

public interface BookRepository extends ReactiveMongoRepository<Book, String> {
    Flux<Book> findAllByAuthorId(String authorId);

    Mono<Void> deleteAllByAuthorId(String authorId);
}
