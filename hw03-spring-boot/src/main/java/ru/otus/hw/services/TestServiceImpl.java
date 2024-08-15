package ru.otus.hw.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.otus.hw.dao.QuestionDao;
import ru.otus.hw.domain.Answer;
import ru.otus.hw.domain.Question;
import ru.otus.hw.domain.Student;
import ru.otus.hw.domain.TestResult;
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

        return writeQuestions(student);
    }

    private TestResult writeQuestions(Student student) {
        TestResult testResult = new TestResult(student);
        try {
            List<Question> questions = questionDao.findAll();
            for (Question question : questions) {
                writeQuestion(question);

                boolean isRightAnswer = readAnswers(question);
                testResult.applyAnswer(question, isRightAnswer);
            }
        } catch (QuestionReadException e) {
            ioService.printLineLocalized("TestService.error.read.questions");
        }

        return testResult;
    }

    private void writeQuestion(Question question) {
        ioService.printLine(question.text());

        int index = 0;
        for (Answer answer : question.answers()) {
            index++;
            ioService.printFormattedLine("\t%d.%s", index, answer.text());
        }
    }

    private boolean readAnswers(Question question) {
        int answerIndex = ioService.readIntForRangeWithPromptLocalized(1,
                question.answers().size(),
                "TestService.answer.the.question",
                "TestService.error.read.answer");

        return question.checkAnswer(answerIndex - 1);
    }
}
