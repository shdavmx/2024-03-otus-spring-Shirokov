package ru.otus.hw.repositories;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import ru.otus.hw.models.Book;

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
        List<Book> books = entityManager.createQuery("select b from Book b " +
                "left join fetch b.genres ", Book.class)
                .getResultList();

        books = entityManager.createQuery("select b from Book b " +
                        "right join fetch b.author a " +
                        "where b in :books", Book.class)
                .setParameter("books", books)
                .getResultList();

        books = entityManager.createQuery("select b from Book b " +
                "left join fetch b.comments c " +
                "where b in :books", Book.class)
                .setParameter("books", books)
                .getResultList();

        return books;
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
                .createQuery("delete from Book b where b.id = :id");
        deleteBookQuery.setParameter("id", id);
        deleteBookQuery.executeUpdate();
    }
}
