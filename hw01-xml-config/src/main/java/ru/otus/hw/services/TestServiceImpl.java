package ru.otus.hw.services;

import lombok.RequiredArgsConstructor;
import ru.otus.hw.dao.QuestionDao;
import ru.otus.hw.domain.Answer;
import ru.otus.hw.domain.Question;

import java.util.List;

@RequiredArgsConstructor
public class TestServiceImpl implements TestService {
    private final IOService ioService;
    private final QuestionDao questionDao;

    @Override
    public void executeTest() {
        ioService.printLine("");
        ioService.printFormattedLine("Please answer the questions below%n");

        List<Question> questions = questionDao.findAll();
        for(Question question : questions) {
            if(question.isFailed()) {
                ioService.printFormattedLine("[Error] Failed to read question: %s", question.getErrorText());
                continue;
            }

            ioService.printLine(question.getQuestion());

            int index = 0;
            for(Answer answer : question.getAnswers()) {
                if(answer.isFailed()) {
                    ioService.printFormattedLine("[Error] Failed to read question: %s", question.getErrorText());
                    continue;
                }

                index++;
                ioService.printFormattedLine("\t%d.%s", index, answer.getAnswer());
            }
        }
    }
}
