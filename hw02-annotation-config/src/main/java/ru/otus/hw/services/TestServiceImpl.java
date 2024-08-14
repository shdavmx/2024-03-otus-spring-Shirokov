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
    private final IOService ioService;

    private final QuestionDao questionDao;

    @Override
    public TestResult executeTestFor(Student student) {
        ioService.printLine("");
        ioService.printFormattedLine("Please answer the questions below%n");

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
            ioService.printLine("[Error] Could not read questions from file");
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
        int answerIndex = ioService.readIntForRangeWithPrompt(1,
                question.answers().size(),
                "Please enter your answer(s)",
                "Something went wrong");

        return question.checkAnswer(answerIndex - 1);
    }
}
