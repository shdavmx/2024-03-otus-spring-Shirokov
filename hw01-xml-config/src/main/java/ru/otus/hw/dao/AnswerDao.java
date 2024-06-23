package ru.otus.hw.dao;

import ru.otus.hw.dao.dto.AnswerDto;
import ru.otus.hw.domain.Answer;

import java.util.List;

public interface AnswerDao {
    List<Answer> toListDomainAnswers(List<AnswerDto> answerDtos);
}
