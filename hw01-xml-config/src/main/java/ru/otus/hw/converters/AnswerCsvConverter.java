package ru.otus.hw.converters;

import com.opencsv.bean.AbstractCsvConverter;
import ru.otus.hw.dao.dto.AnswerDto;
import ru.otus.hw.domain.Answer;

public class AnswerCsvConverter extends AbstractCsvConverter {

    @Override
    public Object convertToRead(String value) {
        var valueArr = value.split("%");
        Answer answer = new Answer(valueArr[0], Boolean.parseBoolean(valueArr[1]));

        return new AnswerDto(answer);
    }
}
