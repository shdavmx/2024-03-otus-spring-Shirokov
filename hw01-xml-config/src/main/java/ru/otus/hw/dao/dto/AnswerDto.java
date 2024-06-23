package ru.otus.hw.dao.dto;

import lombok.Getter;
import lombok.Setter;
import ru.otus.hw.domain.Answer;

@Getter
@Setter
public class AnswerDto {

    private String text;

    private boolean isCorrect;

    public AnswerDto(Answer answer) {
        text = answer.text();
        isCorrect = answer.isCorrect();
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof AnswerDto)) {
            return false;
        }

        return text.equals(((AnswerDto)object).text);
    }

    @Override
    public int hashCode() {
        return text.hashCode();
    }
}
