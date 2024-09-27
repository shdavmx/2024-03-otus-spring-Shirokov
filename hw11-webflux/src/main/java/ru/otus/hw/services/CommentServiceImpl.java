package ru.otus.hw.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.otus.hw.exceptions.EntityNotFoundException;
import ru.otus.hw.models.Book;
import ru.otus.hw.models.Comment;
import ru.otus.hw.models.dto.CommentDto;
import ru.otus.hw.repositories.BookRepository;
import ru.otus.hw.repositories.CommentRepository;

import java.util.Optional;

@RequiredArgsConstructor
@Service
public class CommentServiceImpl implements CommentService {
    private final CommentRepository commentRepository;

    private final BookRepository bookRepository;

    @Override
    public Mono<CommentDto> findById(String id) {
        return commentRepository.findById(id)
                .map(CommentDto::fromDomainObject)
                .switchIfEmpty(Mono.error(
                        new EntityNotFoundException("Comment with id '%s' not found".formatted(id))));
    }

    @Override
    public Flux<CommentDto> findAllByBookId(String bookId) {
        return commentRepository.findAllByBookId(bookId)
                .map(CommentDto::fromDomainObject)
                .switchIfEmpty(Flux.empty());
    }

    @Override
    public Mono<CommentDto> insert(String commentText, String bookId) {
        return save(null, commentText, bookId);
    }

    @Override
    public Mono<CommentDto> update(String id, String commentText, String bookId) {
        return save(id, commentText, bookId);
    }

    @Override
    public Mono<Void> deleteById(String id) {
        return commentRepository.deleteById(id);
    }

    private Mono<CommentDto> save(String id, String commentText, String bookId) {
        return bookRepository.findById(bookId)
                .flatMap(b -> commentRepository.save(new Comment(id, commentText, b)))
                .map(CommentDto::fromDomainObject);
    }
}
