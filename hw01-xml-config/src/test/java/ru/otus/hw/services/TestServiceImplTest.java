package ru.otus.hw.services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.otus.hw.dao.QuestionDao;
import ru.otus.hw.domain.Answer;
import ru.otus.hw.domain.Question;

import java.util.List;

import static org.mockito.BDDMockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("Tests for TestService")
public class TestServiceImplTest {

    @Mock
    private IOService ioService;

    @Mock
    private QuestionDao questionDao;

    private final List<Answer> answersList =
            List.of(
                    new Answer("Answer1", true),
                    new Answer("Answer2", false));

    private TestService testService;

    @BeforeEach
    void setUp() {
        testService = new TestServiceImpl(ioService, questionDao);
    }

    @DisplayName("findAll function")
    @Test
    void shouldExecuteThreeTimes() {
        doNothing().when(ioService).printLine(any());

        given(questionDao.findAll())
                .willReturn(List.of(
                        new Question("Question1", answersList),
                        new Question("Question2", answersList)));

        testService.executeTest();

        verify(ioService, times(3)).printLine(any());
    }
}
