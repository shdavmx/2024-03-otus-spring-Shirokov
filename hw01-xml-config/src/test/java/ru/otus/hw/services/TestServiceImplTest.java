package ru.otus.hw.services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.otus.hw.dao.QuestionDao;
import ru.otus.hw.dao.dto.AnswerDto;
import ru.otus.hw.dao.dto.QuestionDto;
import ru.otus.hw.domain.Answer;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.BDDMockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("Tests for TestService")
public class TestServiceImplTest {
    @Mock
    private IOService ioService;

    @Mock
    private QuestionDao questionDao;

    @InjectMocks
    private TestServiceImpl testService;

    private List<QuestionDto> questions = new ArrayList<>();

    private final List<AnswerDto> answersList =
            List.of(
                    new AnswerDto(new Answer("Answer1", true)),
                    new AnswerDto(new Answer("Answer2", false)));

    @BeforeEach
    public void setUp() {
        questions.clear();

        QuestionDto question1 = new QuestionDto();
        question1.setText("Question1");
        question1.setAnswers(answersList);
        questions.add(question1);

        QuestionDto question2 = new QuestionDto();
        question2.setText("Question2");
        question2.setAnswers(answersList);
        questions.add(question2);
    }

    @DisplayName("findAll function")
    @Test
    void shouldExecuteThreeTimes() {
        doNothing().when(ioService).printLine(any());

        given(questionDao.findAll()).willReturn(questions);

        testService.executeTest();

        verify(ioService, times(3)).printLine(any());
    }
}
