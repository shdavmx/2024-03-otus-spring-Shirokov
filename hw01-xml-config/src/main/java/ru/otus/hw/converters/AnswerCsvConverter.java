package ru.otus.hw.converters;

import com.opencsv.bean.AbstractCsvConverter;
import ru.otus.hw.domain.Answer;

public class AnswerCsvConverter extends AbstractCsvConverter {

    @Override
    public Object convertToRead(String value) {
        Answer answer = new Answer("", false);
        String[] valueArr = value.split("%");

        if (valueArr.length < 2) {
            answer.setFailed(true);
            answer.setErrorText(String.format("Could not parse answer text: '%s'." +
                    "Parsed values less than 2", value));
        } else if (valueArr[0].isEmpty() ||
                 valueArr[1].isEmpty()) {
            answer.setFailed(true);
            answer.setErrorText(String.format("Could not parse answer text: '%s'." +
                    "Some parsed values are empty", value));
        } else {
            answer.setAnswer(valueArr[0]);
            answer.setCorrect(Boolean.parseBoolean(valueArr[1]));
        }

        return answer;
    }
}
