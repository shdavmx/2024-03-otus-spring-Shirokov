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

        return showQuestions(student);
    }

    private TestResult showQuestions(Student student) {
        TestResult testResult = new TestResult(student);
        try {
            List<Question> questions = questionDao.findAll();
            for (Question question : questions) {
                showQuestion(question);

                boolean isRightAnswer = readAnswers(question);
                testResult.applyAnswer(question, isRightAnswer);
            }
        } catch (QuestionReadException e) {
            ioService.printLine(String.format("Could not read questions. Original message: %s",
                    e.getMessage()));
        }

        return testResult;
    }

    private void showQuestion(Question question) {
        ioService.printLine(question.text());

        for (int index = 0; index < question.answers().size(); index++) {
            Answer answer = question.answers().get(index);
            ioService.printFormattedLine("\t%d.%s", index + 1, answer.text());
        }
    }

    private boolean readAnswers(Question question) {
        int answerIndex = ioService.readIntForRangeWithPrompt(1,
                question.answers().size(),
                "Please enter your answer(s)",
                "Out of answers range");

        return question.checkAnswer(answerIndex - 1);
    }
}
