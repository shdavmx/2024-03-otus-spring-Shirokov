package ru.otus.hw.services;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.hw.models.Comment;
import ru.otus.hw.repositories.JpaCommentRepository;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@DisplayName("Integration tests for CommentService")
@DataJpaTest
@Import({JpaCommentRepository.class, CommentServiceImpl.class})
@Transactional(propagation = Propagation.NEVER)
public class CommentServiceIntegrationTest {
    private static final long TEST_BOOK_ID = 1L;

    private static final long TEST_COMMENT_ID = 1L;

    private static final int TEST_COMMENTS_BOOK_SIZE = 2;

    private final List<Comment> testComments = List.of(
            new Comment(1, "Comment_1", null),
            new Comment(2, "Comment_2", null)
    );

    @Autowired
    private CommentServiceImpl commentService;

    @DisplayName("should find comment by id")
    @Test
    public void shouldFindCommentById() {
        Comment expectedComment = testComments.get(0);

        Optional<Comment> actualComment = commentService.findById(TEST_COMMENT_ID);

        Assertions.assertThat(actualComment).isPresent().get()
                .matches(c -> c.getId() == expectedComment.getId())
                .matches(c -> Objects.equals(c.getComment(), expectedComment.getComment()));
    }

    @DisplayName("should find comments by book id")
    @Test
    public void shouldFindCommentsByBookId() {
        List<Comment> actualComments = commentService.findCommentsByBookId(TEST_BOOK_ID);

        Assertions.assertThat(actualComments).isNotNull().hasSize(TEST_COMMENTS_BOOK_SIZE)
                .allMatch(c -> c.getId() > 0)
                .allMatch(c -> !c.getComment().isEmpty());
    }
}
