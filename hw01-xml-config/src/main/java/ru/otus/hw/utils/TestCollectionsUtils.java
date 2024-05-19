package ru.otus.hw.utils;

import ru.otus.hw.dao.dto.QuestionDto;
import ru.otus.hw.domain.Question;

import java.util.ArrayList;
import java.util.List;

public class TestCollectionsUtils {
    public static List<Question> toListQuestions(List<QuestionDto> questionDtos) {
        if(questionDtos == null || questionDtos.isEmpty()) {
            return null;
        }

        List<Question> questions = new ArrayList<>();

        for (QuestionDto questionDto : questionDtos) {
            questions.add(questionDto.toDomainObject());
        }

        return questions;
    }
}
