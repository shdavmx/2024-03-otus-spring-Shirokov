package ru.otus.hw.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.otus.hw.config.TestConfig;
import ru.otus.hw.domain.TestResult;

@Service
@RequiredArgsConstructor
public class ResultServiceImpl implements ResultService {

    private final TestConfig testConfig;

    private final IOService ioService;

    @Override
    public void showResult(TestResult testResult) {
        ioService.printLine("");
        ioService.printLine("Test result: ");
        ioService.printFormattedLine("Student: %s", testResult.getStudent().getFullName());
        ioService.printFormattedLine("Answered questions count: %d",
                testResult.getAnsweredQuestion().size());
        ioService.printFormattedLine("Right answered questions: %d",
                testResult.getRightAnsweredQuestions());

        if (testResult.getRightAnsweredQuestions() >= testConfig.getRightAnswersCountToPass()) {
            ioService.printLine("Congratulation! You passed test!");
            return;
        }

        ioService.printLine("Sorry, you fail test.");
    }
}
