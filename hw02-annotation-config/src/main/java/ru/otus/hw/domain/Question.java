package ru.otus.hw.domain;

import java.util.List;

public record Question(String text, List<Answer> answers) {
    public int countOfRightQuestions() {
        int count = 0;
        for (Answer answer : answers) {
            if (answer.isCorrect()) {
                count++;
            }
        }
        return count;
    }

    public boolean checkAnswer(int answerIndex) {
        Answer answer = answers.get(answerIndex);
        return answer.isCorrect();
    }
}