package ru.otus.hw.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.otus.hw.converters.CommentConverter;
import ru.otus.hw.exceptions.EntityNotFoundException;
import ru.otus.hw.models.Book;
import ru.otus.hw.models.Comment;
import ru.otus.hw.models.dto.CommentDto;
import ru.otus.hw.repositories.BookRepository;
import ru.otus.hw.repositories.CommentRepository;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class CommentServiceImpl implements CommentService {
    private final CommentRepository commentRepository;

    private final BookRepository bookRepository;

    private final CommentConverter commentConverter;

    @Override
    public CommentDto findById(String id) {
        Optional<Comment> comment = commentRepository.findById(id);
        if (comment.isPresent()) {
            return commentConverter.toDto(comment.get());
        }
        throw new EntityNotFoundException("Comment with id '%s' not found".formatted(id));
    }

    @Override
    public List<CommentDto> findAllByBookId(String bookId) {
        return commentRepository.findAllByBookId(bookId).stream()
                .map(commentConverter::toDto)
                .toList();
    }

    @Override
    public CommentDto insert(String commentText, String bookId) {
        return save(null, commentText, bookId);
    }

    @Override
    public CommentDto update(String id, String commentText, String bookId) {
        return save(id, commentText, bookId);
    }

    @Override
    public void deleteById(String id) {
        if (!commentRepository.existsById(id)) {
            throw new EntityNotFoundException("Comment with id '%s' not found".formatted(id));
        }

        commentRepository.deleteById(id);
    }

    private CommentDto save(String id, String commentText, String bookId) {
        Optional<Book> book = bookRepository.findById(bookId);
        if (book.isEmpty()) {
            throw new EntityNotFoundException("Book with id '%s' not found".formatted(bookId));
        }

        Comment comment = new Comment(id, commentText, book.get());
        return commentConverter.toDto(commentRepository.save(comment));
    }
}
