package ru.otus.hw.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.otus.hw.models.dto.CommentDto;
import ru.otus.hw.repositories.CommentRepository;

import java.util.List;

@RequiredArgsConstructor
@Service
public class CommentServiceImpl implements CommentService {
    private final CommentRepository commentRepository;

    @Override
    public List<CommentDto> findAllByBookId(String bookId) {
        return List.of();
    }

    @Override
    public CommentDto insert(String comment, String bookId) {
        return null;
    }

    @Override
    public CommentDto update(String oldComment, String newComment) {
        return null;
    }

    @Override
    public void deleteById(String id) {
        commentRepository.deleteById(id);
    }
}
