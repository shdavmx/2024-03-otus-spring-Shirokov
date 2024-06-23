package ru.otus.hw.services;

import lombok.RequiredArgsConstructor;
import ru.otus.hw.dao.QuestionDao;
import ru.otus.hw.dao.dto.AnswerDto;
import ru.otus.hw.dao.dto.QuestionDto;
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
            List<QuestionDto> questions = questionDao.findAll();
            for (QuestionDto question : questions) {
                ioService.printLine(question.getText());

                int index = 0;
                for (AnswerDto answer : question.getAnswers()) {
                    index++;
                    ioService.printFormattedLine("\t%d.%s", index, answer.getText());
                }
            }
        } catch (QuestionReadException e) {
            ioService.printException(e);
        }
    }
}
