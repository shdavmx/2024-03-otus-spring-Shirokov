package ru.otus.hw.domain;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class TestResult {

    private final Student student;

    private final List<Question> answeredQuestion;

    private int rightAnsweredQuestions;

    public TestResult(Student student) {
        this.student = student;
        this.answeredQuestion = new ArrayList<>();
    }

    public void applyAnswer(Question question, boolean isRightAnswer) {
        answeredQuestion.add(question);
        if (isRightAnswer) {
            rightAnsweredQuestions++;
        }
    }
}
