package ru.otus.hw.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.hw.models.Book;
import ru.otus.hw.models.Comment;
import ru.otus.hw.repositories.BookRepository;
import ru.otus.hw.repositories.CommentRepository;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class CommentServiceImpl implements CommentService {
    private final CommentRepository commentRepository;

    private final BookRepository bookRepository;

    @Override
    public Optional<Comment> findById(long id) {
        return commentRepository.findById(id);
    }

    @Override
    public List<Comment> findByBookId(long id) {
        return commentRepository.findByBookId(id);
    }

    @Transactional
    @Override
    public Comment insert(String comment, long bookId) {
        Book book = bookRepository.findById(bookId);
        return commentRepository.save(new Comment(0, comment, book));
    }

    @Transactional
    @Override
    public Comment update(long id, String comment, long bookId) {
        Book book = bookRepository.findById(bookId);
        return commentRepository.save(new Comment(id, comment, book));
    }

    @Transactional
    @Override
    public void deleteById(long id) {
        commentRepository.deleteById(id);
    }
}
