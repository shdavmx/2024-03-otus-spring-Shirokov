package ru.otus.hw.dao;

import ru.otus.hw.dao.dto.AnswerDto;
import ru.otus.hw.domain.Answer;

import java.util.ArrayList;
import java.util.List;

public class CsvAnswerDao implements AnswerDao {
    @Override
    public List<Answer> toListDomainAnswers(List<AnswerDto> answerDtos) {
        if (answerDtos == null || answerDtos.isEmpty()) {
            return null;
        }

        List<Answer> answers = new ArrayList<>();

        for (AnswerDto answerDto : answerDtos) {
            answers.add(new Answer(answerDto.getText(), answerDto.isCorrect()));
        }

        return answers;
    }
}
