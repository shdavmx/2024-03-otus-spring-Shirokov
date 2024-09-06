package ru.otus.hw.repositories;

import org.assertj.core.api.Assertions;
import org.hibernate.SessionFactory;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.Import;
import ru.otus.hw.models.Author;
import ru.otus.hw.models.Book;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@DisplayName("Tests for JpaBookRepository")
@DataJpaTest
@Import(JpaBookRepository.class)
public class JpaBookRepositoryTest {
    private static final long TEST_BOOK_ID = 1L;

    private static final String TEST_BOOK_TITLE = "updatedTitle";

    private static final int EXPECTED_BOOK_SIZE = 3;

    private static final int EXPECTED_QUERY_COUNT = 2;

    @Autowired
    private JpaBookRepository jpaBookRepository;

    @Autowired
    private TestEntityManager testEntityManager;

    @DisplayName("should find expected book id")
    @Test
    public void shouldFindExpectedBookById() {
        Book actualBook = jpaBookRepository.findById(TEST_BOOK_ID);
        Book expectedBook = testEntityManager.find(Book.class, TEST_BOOK_ID);

        Assertions.assertThat(actualBook).isNotNull()
                .usingRecursiveComparison().isEqualTo(expectedBook);
    }

    @DisplayName("should find all books with full info")
    @Test
    public void shouldFindAllBooksWithFullInfo() {
        SessionFactory sessionFactory = testEntityManager.getEntityManager().getEntityManagerFactory()
                .unwrap(SessionFactory.class);
        sessionFactory.getStatistics().setStatisticsEnabled(true);

        List<Book> actualBooks = jpaBookRepository.findAll();

        Assertions.assertThat(actualBooks).isNotNull().hasSize(EXPECTED_BOOK_SIZE)
                .allMatch(b -> b.getId() > 0)
                .allMatch(b -> !b.getTitle().isEmpty())
                .allMatch(b -> b.getAuthor() != null)
                .allMatch(b -> b.getGenres() != null && !b.getGenres().isEmpty());

        Assertions.assertThat(sessionFactory.getStatistics().getPrepareStatementCount())
                .isEqualTo(EXPECTED_QUERY_COUNT);
    }

    @DisplayName("should save new book")
    @Test
    public void shouldSaveNewBook() {
        Author testAuthor = new Author();
        testAuthor.setId(0);
        testAuthor.setFullName("test");
        Book newBook = new Book(0, "testTile",
                testAuthor, new ArrayList<>());

        Book savedBook = jpaBookRepository.save(newBook);
        Assertions.assertThat(savedBook.getId()).isGreaterThan(0);

        Book actualBook = testEntityManager.find(Book.class, savedBook.getId());
        Assertions.assertThat(actualBook).isNotNull()
                .matches(b -> !b.getTitle().isEmpty())
                .matches(b -> b.getAuthor() != null);
    }

    @DisplayName("should update existing book")
    @Test
    public void shouldUpdateExistingBook() {
        Book findBook = jpaBookRepository.findById(TEST_BOOK_ID);
        findBook.setTitle(TEST_BOOK_TITLE);
        Book actualBook = jpaBookRepository.save(findBook);

        Assertions.assertThat(actualBook.getTitle()).isEqualTo(TEST_BOOK_TITLE);
    }

    @DisplayName("should delete book by id")
    @Test
    public void shouldDeleteBookById() {
        Book deleteBook = testEntityManager.find(Book.class, TEST_BOOK_ID);
        Assertions.assertThat(deleteBook).isNotNull();
        testEntityManager.detach(deleteBook);

        jpaBookRepository.deleteById(TEST_BOOK_ID);
        Book actualBook = testEntityManager.find(Book.class, TEST_BOOK_ID);

        Assertions.assertThat(actualBook).isNull();
    }
}
