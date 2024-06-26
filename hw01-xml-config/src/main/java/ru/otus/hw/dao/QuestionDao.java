package ru.otus.hw.dao;

import ru.otus.hw.dao.dto.QuestionDto;
import ru.otus.hw.domain.Question;

import java.util.List;

public interface QuestionDao {
    List<Question> findAll();

    List<Question> toDomainObjects(List<QuestionDto> questionDtoList);
}
