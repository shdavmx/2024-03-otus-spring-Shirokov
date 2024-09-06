package ru.otus.hw.repositories;

import jakarta.persistence.PersistenceContext;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityGraph;
import jakarta.persistence.TypedQuery;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import ru.otus.hw.models.Book;

import java.util.Map;
import java.util.HashMap;
import java.util.List;

import static org.springframework.data.jpa.repository.EntityGraph.EntityGraphType.FETCH;

@RequiredArgsConstructor
@Repository
public class JpaBookRepository implements BookRepository {
    @PersistenceContext
    private final EntityManager entityManager;

    @Override
    public Book findById(long id) {
        EntityGraph<?> graph = entityManager.getEntityGraph("hw-book-author-entity-graph");
        Map<String, Object> queryParams = new HashMap<>();
        queryParams.put(FETCH.getKey(), graph);
        return entityManager.find(Book.class, id, queryParams);
    }

    @Override
    public List<Book> findAll() {
        EntityGraph<?> graph = entityManager.getEntityGraph("hw-book-author-entity-graph");
        TypedQuery<Book> query = entityManager.createQuery(
                "select b from Book b ", Book.class);
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
