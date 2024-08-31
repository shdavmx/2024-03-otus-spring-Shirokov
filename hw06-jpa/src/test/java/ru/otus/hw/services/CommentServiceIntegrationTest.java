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

import java.util.Objects;
import java.util.Optional;

@DisplayName("Tests for CommentService")
@DataJpaTest
@Import({JpaCommentRepository.class, CommentServiceImpl.class})
@Transactional(propagation = Propagation.NOT_SUPPORTED)
public class CommentServiceIntegrationTest {
    private static final long TEST_COMMENT_ID = 1L;

    @Autowired
    private CommentServiceImpl commentService;

    @DisplayName("should find comment by id")
    @Test
    public void shouldFindCommentById() {
        Comment expectedComment =
                new Comment(1, "Comment_1", null);

        Optional<Comment> actualComment = commentService.findById(TEST_COMMENT_ID);

        Assertions.assertThat(actualComment).isPresent().get()
                .matches(c -> c.getId() == expectedComment.getId())
                .matches(c -> Objects.equals(c.getComment(), expectedComment.getComment()));
    }
}
