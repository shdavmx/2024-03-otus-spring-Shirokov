package ru.otus.hw.services;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
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
    @Captor
    ArgumentCaptor<String> message;

    @Mock
    private IOService ioService;

    @Mock
    private QuestionDao questionDao;

    @InjectMocks
    private TestServiceImpl testService;

    private final List<Answer> answersList =
            List.of(
                    new Answer("Answer1", true),
                    new Answer("Answer2", false));

    @DisplayName("Should print questions list in the correct way")
    @Test
    void shouldPrintQuestionsListInTheCorrectWay() {
        String[] expectedPrintedMessages = new String[] {"", "Question1", "Question2"};

        doNothing().when(ioService).printLine(message.capture());
        given(questionDao.findAll()).willReturn(List.of(
                new Question("Question1", answersList),
                new Question("Question2", answersList)
        ));

        testService.executeTest();

        Assertions.assertArrayEquals(message.getAllValues().toArray(), expectedPrintedMessages);
    }
}
