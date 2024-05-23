package ru.otus.hw.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DisplayName("Class Question")
public class QuestionTest {

    private final List<Answer> answerList =
            List.of(new Answer("Answer", true));

    @DisplayName("has correct constructor")
    @Test
    void shouldCorrectConstructor() {
        Question question = new Question("Question", answerList);

        assertEquals("Question", question.getQuestion());
        assertEquals("Answer", question.getAnswers().get(0).getAnswer());
    }
}
