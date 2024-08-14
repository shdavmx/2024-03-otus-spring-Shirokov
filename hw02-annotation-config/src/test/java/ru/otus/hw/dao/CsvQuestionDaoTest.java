package ru.otus.hw.dao;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.otus.hw.config.TestFileNameProvider;
import ru.otus.hw.domain.Answer;
import ru.otus.hw.domain.Question;

import java.util.List;

@DisplayName("Integration test for CsvQuestionDao")
public class CsvQuestionDaoTest {
    private CsvQuestionDao csvQuestionDao;

    private final List<Answer> answersList =
            List.of(
                    new Answer("Answer1", true),
                    new Answer("Answer2", false));

    @BeforeEach
    public void init() {
        TestFileNameProvider fileNameProvider = () -> "questions.csv";

        csvQuestionDao = new CsvQuestionDao(fileNameProvider);
    }

    @DisplayName("Should correct read questions and answers from csv file")
    @Test
    void shouldReadQuestionsCorrectly() {
        List<Question> expectedQuestions = List.of(new Question("Question1?", answersList),
                new Question("Question2?", answersList));

        List<Question> actualQuestions = csvQuestionDao.findAll();

        Assertions.assertEquals(expectedQuestions.get(0).text(), actualQuestions.get(0).text());
        Assertions.assertEquals(expectedQuestions.get(1).text(), actualQuestions.get(1).text());

        Assertions.assertEquals(expectedQuestions.get(0).answers().get(0).text(),
                actualQuestions.get(0).answers().get(0).text());
        Assertions.assertEquals(expectedQuestions.get(0).answers().get(0).isCorrect(),
                actualQuestions.get(0).answers().get(0).isCorrect());
        Assertions.assertEquals(expectedQuestions.get(0).answers().get(1).text(),
                actualQuestions.get(0).answers().get(1).text());
        Assertions.assertEquals(expectedQuestions.get(0).answers().get(1).isCorrect(),
                actualQuestions.get(0).answers().get(1).isCorrect());

        Assertions.assertEquals(expectedQuestions.get(1).answers().get(0).text(),
                actualQuestions.get(1).answers().get(0).text());
        Assertions.assertEquals(expectedQuestions.get(1).answers().get(0).isCorrect(),
                actualQuestions.get(1).answers().get(0).isCorrect());
        Assertions.assertEquals(expectedQuestions.get(1).answers().get(1).text(),
                actualQuestions.get(1).answers().get(1).text());
        Assertions.assertEquals(expectedQuestions.get(1).answers().get(1).isCorrect(),
                actualQuestions.get(1).answers().get(1).isCorrect());
    }
}
