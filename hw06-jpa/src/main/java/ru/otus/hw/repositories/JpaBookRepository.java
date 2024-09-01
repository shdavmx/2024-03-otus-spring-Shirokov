package ru.otus.hw.repositories;

import jakarta.persistence.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import ru.otus.hw.models.Book;

import java.util.List;
import java.util.Optional;

import static org.springframework.data.jpa.repository.EntityGraph.EntityGraphType.FETCH;

@RequiredArgsConstructor
@Repository
public class JpaBookRepository implements BookRepository {
    @PersistenceContext
    private final EntityManager entityManager;

    @Override
    public Optional<Book> findById(long id) {
        EntityGraph<?> graph = entityManager.getEntityGraph("hw-book-author-entity-graph");
        TypedQuery<Book> query = entityManager.createQuery(
                "select b from Book b " +
                        "left join fetch b.genres " +
                        "where b.id = :id", Book.class);
        query.setHint(FETCH.getKey(), graph);
        query.setParameter("id", id);
        return query.getResultList().stream().findFirst();
    }

    @Override
    public List<Book> findAll() {
        EntityGraph<?> graph = entityManager.getEntityGraph("hw-book-author-entity-graph");
        TypedQuery<Book> query = entityManager.createQuery(
                "select b from Book b " +
                   "left join fetch b.genres", Book.class);
        query.setHint(FETCH.getKey(), graph);
        return query.getResultList();
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
        Book book = entityManager.find(Book.class, id);
        entityManager.remove(book);
    }
}
