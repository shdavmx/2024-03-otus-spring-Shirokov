package ru.otus.hw.dao;

import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import ru.otus.hw.config.TestFileNameProvider;
import ru.otus.hw.dao.dto.QuestionDto;
import ru.otus.hw.domain.Question;
import ru.otus.hw.exceptions.QuestionReadException;
import ru.otus.hw.utils.TestFileResourceUtils;

import java.io.Reader;
import java.util.ArrayList;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class CsvQuestionDao implements QuestionDao {

    private final TestFileNameProvider fileNameProvider;

    @Override
    public List<Question> findAll() {
        List<Question> questions;
        try (Reader questionsReader =
            TestFileResourceUtils.getResourceFileReader(getClass(),fileNameProvider.getTestFileName())) {
            CsvToBean<QuestionDto> builder = new CsvToBeanBuilder<QuestionDto>(questionsReader)
                    .withType(QuestionDto.class)
                    .withSeparator(';')
                    .withSkipLines(1)
                    .withIgnoreEmptyLine(true)
                    .build();

            List<QuestionDto> questionDtoList = builder.parse();
            questions = toDomainObjects(questionDtoList);
        } catch (Exception e) {
            throw  new QuestionReadException("Could not read questions", e);
        }

        return questions;
    }

    @Override
    public List<Question> toDomainObjects(List<QuestionDto> questionDtoList) {
        List<Question> questions = new ArrayList<>();
        for (QuestionDto questionDto : questionDtoList) {
            questions.add(questionDto.toDomainObject());
        }

        return  questions;
    }
}
