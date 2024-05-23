package ru.otus.hw.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DisplayName("Class Answer")
public class AnswerTest {

    @DisplayName("has correct constructor")
    @Test
    void shouldHaveCorrectConstructor() {
        Answer answer = new Answer("Answer", true);

        assertEquals("Answer", answer.getAnswer());
        assertTrue(answer.isCorrect());
    }
}
