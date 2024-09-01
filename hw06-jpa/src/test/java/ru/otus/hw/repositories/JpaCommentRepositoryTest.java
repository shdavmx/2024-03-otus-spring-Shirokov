package ru.otus.hw.repositories;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.Import;
import ru.otus.hw.models.Comment;

import java.util.List;
import java.util.Optional;

@DisplayName("Tests for JpaCommentRepository")
@DataJpaTest
@Import(JpaCommentRepository.class)
public class JpaCommentRepositoryTest {
    private static final long TEST_COMMENT_ID = 1L;

    private static final long TEST_BOOK_ID = 1L;

    private static final int TEST_COMMENTS_BOOK_SIZE = 2;

    @Autowired
    private JpaCommentRepository jpaCommentRepository;

    @Autowired
    private TestEntityManager testEntityManager;

    @DisplayName("should find expected comment by id")
    @Test
    public void shouldFindExpectedCommentById() {
        Optional<Comment> actualComment = jpaCommentRepository.findById(TEST_COMMENT_ID);
        Comment expectedComment = testEntityManager.find(Comment.class, TEST_COMMENT_ID);

        Assertions.assertThat(actualComment).isPresent().get()
                .usingRecursiveComparison().isEqualTo(expectedComment);
    }

    @DisplayName("should find all comments by book id")
    @Test
    public void shouldFindAllCommentsByBookId() {
        List<Comment> comments = jpaCommentRepository.findCommentsByBookId(TEST_BOOK_ID);

        Assertions.assertThat(comments).isNotNull().hasSize(TEST_COMMENTS_BOOK_SIZE)
                .allMatch(c -> c.getId() > 0)
                .allMatch(c -> !c.getComment().isEmpty());
    }
}
