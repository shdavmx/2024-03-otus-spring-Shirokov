package ru.otus.hw.models.repositories;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.Import;
import ru.otus.hw.models.Comment;
import ru.otus.hw.repositories.JpaCommentRepository;

import java.util.Optional;

@DisplayName("Tests for JpaCommentRepository")
@DataJpaTest
@Import(JpaCommentRepository.class)
public class JpaCommentRepositoryTest {
    private static final long TEST_COMMENT_ID = 1L;

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
}
