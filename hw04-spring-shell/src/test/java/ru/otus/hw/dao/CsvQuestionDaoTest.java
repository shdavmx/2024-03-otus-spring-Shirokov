package ru.otus.hw.dao;

import org.assertj.core.api.Assertions;
import org.assertj.core.api.recursive.comparison.RecursiveComparisonConfiguration;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import ru.otus.hw.config.TestFileNameProvider;
import ru.otus.hw.domain.Answer;
import ru.otus.hw.domain.Question;

import java.util.List;

@DisplayName("Test for CsvQuestionDao")
@SpringBootTest(classes = { TestFileNameProvider.class, CsvQuestionDao.class })
public class CsvQuestionDaoTest {
    @MockBean
    private TestFileNameProvider fileNameProvider;

    @Autowired
    private CsvQuestionDao csvQuestionDao;

    private final List<Answer> answersList =
            List.of(
                    new Answer("Answer1", true),
                    new Answer("Answer2", false));

    @DisplayName("Should correct read questions and answers from csv file")
    @Test
    void shouldReadQuestionsCorrectly() {
        RecursiveComparisonConfiguration configuration = RecursiveComparisonConfiguration.builder()
                .build();
        Mockito.when(fileNameProvider.getTestFileName()).thenReturn("questions.csv");

        List<Question> expectedQuestions = List.of(new Question("Question1?", answersList),
                new Question("Question2?", answersList));

        List<Question> actualQuestions = csvQuestionDao.findAll();

        Assertions.assertThat(actualQuestions).usingRecursiveFieldByFieldElementComparator(configuration)
                .containsAll(expectedQuestions);
    }
}
