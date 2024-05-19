package ru.otus.hw.dao;

import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;
import lombok.RequiredArgsConstructor;
import ru.otus.hw.config.TestFileNameProvider;
import ru.otus.hw.dao.dto.QuestionDto;
import ru.otus.hw.domain.Question;
import ru.otus.hw.exceptions.QuestionReadException;
import ru.otus.hw.utils.TestCollectionsUtils;
import ru.otus.hw.utils.TestFileResourceUtils;

import java.io.Reader;
import java.util.List;

@RequiredArgsConstructor
public class CsvQuestionDao implements QuestionDao {
    private final TestFileNameProvider fileNameProvider;

    @Override
    public List<Question> findAll() throws QuestionReadException {
        List<QuestionDto> questions;
        try {
            Reader questionsReader = TestFileResourceUtils.getResourceFileReader(getClass(),
                    fileNameProvider.getTestFileName());
            CsvToBean<QuestionDto> builder = new CsvToBeanBuilder(questionsReader).withType(QuestionDto.class)
                    .withSeparator(';')
                    .withSkipLines(1)
                    .withIgnoreEmptyLine(true)
                    .build();

            questions = builder.parse();
        }
        catch (Exception e) {
            throw  new QuestionReadException("File not found", new Exception());
        }

        return TestCollectionsUtils.toListQuestions(questions);
    }
}
