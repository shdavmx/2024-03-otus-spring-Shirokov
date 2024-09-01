package ru.otus.hw.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.otus.hw.dao.QuestionDao;
import ru.otus.hw.domain.Answer;
import ru.otus.hw.domain.Question;
import ru.otus.hw.domain.Student;
import ru.otus.hw.domain.TestResult;
import ru.otus.hw.exceptions.MaxAttemptInputException;
import ru.otus.hw.exceptions.QuestionReadException;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TestServiceImpl implements TestService {
    private final LocalizedIOService ioService;

    private final QuestionDao questionDao;

    @Override
    public TestResult executeTestFor(Student student) {
        ioService.printLine("");
        ioService.printLineLocalized("TestService.answer.the.questions");

        return askQuestions(student);
    }

    private TestResult askQuestions(Student student) {
        TestResult testResult = new TestResult(student);
        try {
            List<Question> questions = questionDao.findAll();
            for (Question question : questions) {
                askQuestion(question);

                boolean isRightAnswer = readAnswers(question);
                testResult.applyAnswer(question, isRightAnswer);
            }
        } catch (QuestionReadException ex) {
            ioService.printLineLocalized("TestService.error.read.questions");
        } catch (MaxAttemptInputException ex) {
            ioService.printLineLocalized("IOService.error.max.attempts.inputs");
        }

        return testResult;
    }

    private void askQuestion(Question question) {
        ioService.printLine(question.text());

        for (int index = 0; index < question.answers().size(); index++) {
            Answer answer = question.answers().get(index);
            ioService.printFormattedLine("\t%d.%s", index + 1, answer.text());
        }
    }

    private boolean readAnswers(Question question) {
        int answerIndex = ioService.readIntForRangeWithPromptLocalized(1,
                question.answers().size(),
                "TestService.answer.the.question",
                "TestService.error.read.answer");

        Answer answer = question.answers().get(answerIndex - 1);
        return answer.isCorrect();
    }
}
