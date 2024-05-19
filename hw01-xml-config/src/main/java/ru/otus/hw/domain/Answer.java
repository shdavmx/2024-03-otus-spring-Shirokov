package ru.otus.hw.domain;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Answer extends BaseDomain {
    private String answer;
    private boolean isCorrect;

    public Answer(String answer, boolean isCorrect) {
        this.answer = answer;
        this.isCorrect = isCorrect;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof Answer)) {
            return false;
        }

        return answer.equals(((Answer)object).answer);
    }

    @Override
    public int hashCode() {
        return answer.hashCode();
    }
}
