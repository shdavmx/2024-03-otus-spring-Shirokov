package ru.otus.hw.repositories;

import jakarta.persistence.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import ru.otus.hw.models.Comment;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Repository
public class JpaCommentRepository implements CommentRepository {
    @PersistenceContext
    private final EntityManager entityManager;

    @Override
    public Optional<Comment> findById(long id) {
        return Optional.of(entityManager.find(Comment.class, id));
    }

    @Override
    public List<Comment> findCommentsByBookId(long id) {
        TypedQuery<Comment> query = entityManager.createQuery(
                "select c from Comment c " +
                   "where c.book.id = :id", Comment.class);
        query.setParameter("id", id);
        return query.getResultList();
    }
}
