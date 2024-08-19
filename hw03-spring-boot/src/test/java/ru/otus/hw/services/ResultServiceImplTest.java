package ru.otus.hw.services;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.otus.hw.config.TestConfig;
import ru.otus.hw.domain.Answer;
import ru.otus.hw.domain.Question;
import ru.otus.hw.domain.Student;
import ru.otus.hw.domain.TestResult;

import java.util.List;

@ExtendWith(MockitoExtension.class)
@DisplayName("Tests for ResultService")
public class ResultServiceImplTest {
    @Captor
    private ArgumentCaptor<String> message;

    @Mock
    private LocalizedIOService ioService;

    @Mock
    private TestConfig testConfig;

    @InjectMocks
    private ResultServiceImpl resultService;

    private TestResult testResult;

    @BeforeEach
    void init() {
        Student testStudent = new Student("testName", "testLastName");
        testResult = new TestResult(testStudent);
    }

    @DisplayName("Should display success passed message")
    @Test
    void shouldDisplaySuccessMessage() {
        String expectedMessage = "ResultService.passed.test";
        testResult.applyAnswer(new Question("Question1",
                List.of(new Answer("Answer 1", true))), true);
        testResult.applyAnswer(new Question("Question2",
                List.of(new Answer("Answer 2", true))), true);
        testResult.applyAnswer(new Question("Question3",
                List.of(new Answer("Answer 3", true))), true);

        Mockito.when(testConfig.getRightAnswersCountToPass()).thenReturn(3);
        Mockito.doNothing().when(ioService).printLineLocalized(message.capture());

        resultService.showResult(testResult);
        String actualMessage = message.getAllValues().get(message.getAllValues().size() - 1);

        Assertions.assertEquals(expectedMessage, actualMessage);
    }
}
