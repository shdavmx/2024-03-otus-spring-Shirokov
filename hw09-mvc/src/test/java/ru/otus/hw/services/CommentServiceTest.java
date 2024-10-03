package ru.otus.hw.services;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.hw.exceptions.EntityNotFoundException;
import ru.otus.hw.models.Comment;
import ru.otus.hw.models.dto.CommentDto;

import java.util.List;
import java.util.Objects;

@DisplayName("Integration tests for CommentService")
@DataMongoTest
@Import({
        CommentServiceImpl.class
})
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class CommentServiceTest {
    private static final String TEST_BOOK_ID = "1";

    private static final String TEST_COMMENT_ID = "1";

    private static final int TEST_COMMENTS_BOOK_SIZE = 2;

    private final List<Comment> testComments = List.of(
            new Comment("1", "Comment_1", null),
            new Comment("2", "Comment_2", null)
    );

    @Autowired
    private CommentServiceImpl commentService;

    @DisplayName("should find comment by id")
    @Test
    public void shouldFindCommentById() {
        Comment expectedComment = testComments.get(0);

        CommentDto actualComment = commentService.findById(TEST_COMMENT_ID);

        Assertions.assertThat(actualComment).isNotNull()
                .matches(c -> c.getId().equals(expectedComment.getId()))
                .matches(c -> Objects.equals(c.getComment(), expectedComment.getComment()));
    }

    @Transactional(propagation = Propagation.NEVER)
    @DisplayName("should find comments by book id")
    @Test
    public void shouldFindCommentsByBookId() {
        List<CommentDto> actualComments = commentService.findAllByBookId(TEST_BOOK_ID);

        Assertions.assertThat(actualComments).isNotNull().hasSize(TEST_COMMENTS_BOOK_SIZE)
                .allMatch(c -> !c.getId().isEmpty())
                .allMatch(c -> !c.getComment().isEmpty());
    }

    @Rollback
    @DisplayName("should insert new comment")
    @Test
    public void shouldInsertNewComment() {
        CommentDto actualComment = commentService.insert("newComment", TEST_BOOK_ID);

        Assertions.assertThat(actualComment).isNotNull()
                .matches(c -> !c.getId().isEmpty())
                .matches(c -> c.getComment().equals("newComment"));
    }

    @Rollback
    @DisplayName("should update existing comment")
    @Test
    public void shouldUpdateExistingComment() {
        CommentDto actualComment = commentService.update(TEST_COMMENT_ID, "updatedComment", TEST_BOOK_ID);

        Assertions.assertThat(actualComment).isNotNull()
                .matches(c -> c.getId().equals(TEST_COMMENT_ID))
                .matches(c -> c.getComment().equals("updatedComment"));
    }

    @Rollback
    @DisplayName("should delete comment by id")
    @Test
    public void shouldDeleteCommentById() {
        commentService.deleteById(TEST_COMMENT_ID);

        Assertions.assertThatExceptionOfType(EntityNotFoundException.class)
                .isThrownBy(() -> commentService.findById(TEST_COMMENT_ID));
    }
}
