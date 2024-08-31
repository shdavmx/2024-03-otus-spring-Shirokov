package ru.otus.hw.services;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.hw.models.Comment;
import ru.otus.hw.repositories.CommentRepository;
import ru.otus.hw.services.CommentServiceImpl;

import java.util.Optional;

@DisplayName("Tests for CommentService")
@SpringBootTest(classes = { CommentServiceImpl.class })
@Transactional(propagation = Propagation.NOT_SUPPORTED)
public class CommentServiceTest {
    private static final long TEST_COMMENT_ID = 1L;

    @MockBean
    private CommentRepository commentRepository;

    @Autowired
    private CommentServiceImpl commentService;

    @DisplayName("should return expected comment by id")
    @Test
    public void shouldReturnExpectedCommentById() {
        Comment expectedComment = new Comment(1, "test", null);

        Mockito.when(commentRepository.findById(TEST_COMMENT_ID))
                .thenReturn(Optional.of(expectedComment));

        Optional<Comment> actualComment = commentService.findById(TEST_COMMENT_ID);

        Assertions.assertThat(actualComment).isPresent().get()
                .usingRecursiveComparison().isEqualTo(expectedComment);
    }
}
