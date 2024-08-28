package ru.otus.hw.repositories;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import ru.otus.hw.exceptions.EntityNotFoundException;
import ru.otus.hw.models.Book;
import ru.otus.hw.models.Comment;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Repository
public class JpaBookRepository implements BookRepository {
    @PersistenceContext
    private final EntityManager entityManager;

    @Override
    public Optional<Book> findById(long id) {
        return Optional.ofNullable(entityManager.find(Book.class, id));
    }

    @Override
    public List<Book> findAll() {
        return entityManager.createQuery("select b from Book b ", Book.class)
                .getResultList();
    }

    @Override
    public List<Comment> findCommentsByBookId(long id) {
        Optional<Book> book = findById(id);
        if (book.isPresent()) {
            return book.get().getComments();
        }
        throw new EntityNotFoundException("Book with id %d not found".formatted(id));
    }

    @Override
    public Book save(Book book) {
        if (book.getId() == 0) {
            entityManager.persist(book);
            return book;
        }
        return entityManager.merge(book);
    }

    @Override
    public void deleteById(long id) {
        Query deleteBookQuery = entityManager
                .createQuery("delete b from Book b where b.id = :id");
        deleteBookQuery.setParameter("id", id);
        deleteBookQuery.executeUpdate();
    }
}
