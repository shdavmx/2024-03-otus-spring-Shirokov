package ru.otus.hw.services;

import lombok.RequiredArgsConstructor;
import ru.otus.hw.dao.QuestionDao;
import ru.otus.hw.domain.Answer;
import ru.otus.hw.domain.Question;
import ru.otus.hw.exceptions.QuestionReadException;

import java.util.List;

@RequiredArgsConstructor
public class TestServiceImpl implements TestService {
    private final IOService ioService;

    private final QuestionDao questionDao;

    @Override
    public void executeTest() {
        ioService.printLine("");
        ioService.printFormattedLine("Please answer the questions below%n");

        try {
            List<Question> questions = questionDao.findAll();
            for (Question question : questions) {
                ioService.printLine(question.text());

                int index = 0;
                for (Answer answer : question.answers()) {
                    index++;
                    ioService.printFormattedLine("\t%d.%s", index, answer.text());
                }
            }
        } catch (QuestionReadException e) {
            ioService.printException(e);
        }
    }
}
