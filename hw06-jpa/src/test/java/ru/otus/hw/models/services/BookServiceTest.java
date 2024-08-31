package ru.otus.hw.models.services;

import jakarta.transaction.Transactional;
import org.assertj.core.api.Assertions;
import org.assertj.core.api.recursive.comparison.RecursiveComparisonConfiguration;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import ru.otus.hw.models.Author;
import ru.otus.hw.models.Book;
import ru.otus.hw.models.Comment;
import ru.otus.hw.models.Genre;
import ru.otus.hw.repositories.AuthorRepository;
import ru.otus.hw.repositories.BookRepository;
import ru.otus.hw.repositories.GenreRepository;
import ru.otus.hw.services.BookServiceImpl;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@DisplayName("Tests for BookService")
@SpringBootTest(classes = {BookServiceImpl.class})
@Transactional(Transactional.TxType.NOT_SUPPORTED)
public class BookServiceTest {
    private static final long TEST_BOOK_ID = 1L;

    private final Author testAuthor =
            new Author(1, "test1", new ArrayList<>());

    private final List<Comment> testComments =
            List.of(new Comment(1, "test1", null));

    private final List<Genre> testGenres =
            List.of(new Genre(1, "test1"));

    private final List<Book> testBooks =
            List.of(new Book(1, "test1", testAuthor, testComments, testGenres),
                    new Book(2, "test2", testAuthor, testComments, testGenres));

    @Captor
    private ArgumentCaptor<Long> passedLongValue;

    @MockBean
    private GenreRepository genreRepository;

    @MockBean
    private AuthorRepository authorRepository;

    @MockBean
    private BookRepository bookRepository;

    @Autowired
    private BookServiceImpl bookService;

    @DisplayName("should find expected book by id")
    @Test
    public void shouldFindExpectedBookById() {
        Book expectedBook = testBooks.get(0);

        Mockito.when(bookRepository.findById(TEST_BOOK_ID))
                .thenReturn(Optional.of(expectedBook));

        Optional<Book> actualBook = bookRepository.findById(TEST_BOOK_ID);

        Assertions.assertThat(actualBook).isPresent().get()
                .usingRecursiveComparison().isEqualTo(expectedBook);
    }

    @DisplayName("should find all expected books")
    @Test
    public void shouldFindAllExpectedBooks() {
        RecursiveComparisonConfiguration configuration =
                RecursiveComparisonConfiguration.builder().build();

        Mockito.when(bookRepository.findAll())
                .thenReturn(testBooks);

        List<Book> actualBooks = bookService.findAll();

        Assertions.assertThat(actualBooks).usingRecursiveFieldByFieldElementComparator(configuration)
                .containsAll(testBooks);
    }

    @DisplayName("should insert new book")
    @Test
    public void shouldInsertNewBook() {
        Book expectedBook = testBooks.get(0);
        long authorId = expectedBook.getAuthor().getId();
        Set<Long> genreIds = expectedBook.getGenres().stream()
                .map(Genre::getId).collect(Collectors.toSet());

        Mockito.when(authorRepository.findById(authorId))
                .thenReturn(Optional.of(testAuthor));
        Mockito.when(genreRepository.findAllByIds(genreIds))
                .thenReturn(testGenres);
        Mockito.when(bookRepository.save(
                new Book(0, expectedBook.getTitle(), testAuthor, new ArrayList<>(), testGenres)))
                .thenReturn(expectedBook);

        Book actualBook = bookService.insert(expectedBook.getTitle(), authorId, genreIds);

        Assertions.assertThat(actualBook).isNotNull()
                .usingRecursiveComparison().isEqualTo(expectedBook);
    }

    @DisplayName("should update existing book")
    @Test
    public void shouldUpdateExistingBook() {
        Book expectedBook = testBooks.get(0);
        long authorId = expectedBook.getAuthor().getId();
        Set<Long> genreIds = expectedBook.getGenres().stream()
                .map(Genre::getId).collect(Collectors.toSet());

        Mockito.when(authorRepository.findById(authorId))
                .thenReturn(Optional.of(testAuthor));
        Mockito.when(genreRepository.findAllByIds(genreIds))
                .thenReturn(testGenres);
        Mockito.when(bookRepository.findById(TEST_BOOK_ID))
                .thenReturn(Optional.of(expectedBook));
        Mockito.when(bookRepository.save(
                        new Book(TEST_BOOK_ID, expectedBook.getTitle(), testAuthor, testComments, testGenres)))
                .thenReturn(expectedBook);

        Book actualBook = bookService.update(expectedBook.getId(), expectedBook.getTitle(), authorId, genreIds);

        Assertions.assertThat(actualBook).isNotNull()
                .usingRecursiveComparison().isEqualTo(expectedBook);
    }

    @DisplayName("should return expected book comments")
    @Test
    public void shouldReturnExpectedBookComments() {
        RecursiveComparisonConfiguration configuration =
                RecursiveComparisonConfiguration.builder().build();

        Book expectedBook = testBooks.get(0);

        Mockito.when(bookRepository.findById(TEST_BOOK_ID))
                .thenReturn(Optional.of(expectedBook));

        List<Comment> actualComments = bookService.findCommentsByBookId(TEST_BOOK_ID);

        Assertions.assertThat(actualComments).usingRecursiveFieldByFieldElementComparator(configuration)
                .containsAll(testComments);
    }

    @DisplayName("should pass expected id to delete method")
    @Test
    public void shouldPassExpectedIdToDeleteMethod() {
        Mockito.doNothing().when(bookRepository).deleteById(passedLongValue.capture());

        bookService.deleteById(TEST_BOOK_ID);

        Assertions.assertThat(passedLongValue.getValue()).isEqualTo(TEST_BOOK_ID);
    }
}
