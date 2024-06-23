package ru.otus.hw.dao.dto;

import com.opencsv.bean.CsvBindAndSplitByPosition;
import com.opencsv.bean.CsvBindByPosition;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import ru.otus.hw.converters.AnswerCsvConverter;

import java.util.ArrayList;
import java.util.List;

@Data
@Getter
@Setter
public class QuestionDto {
    @CsvBindByPosition(position = 0)
    private String text;

    @CsvBindAndSplitByPosition(position = 1, collectionType = ArrayList.class, elementType = AnswerDto.class,
        converter = AnswerCsvConverter.class, splitOn = "\\|")
    private List<AnswerDto> answers;
}
