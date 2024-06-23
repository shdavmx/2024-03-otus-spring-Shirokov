package ru.otus.hw.dao;

import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;
import lombok.RequiredArgsConstructor;
import ru.otus.hw.config.TestFileNameProvider;
import ru.otus.hw.dao.dto.AnswerDto;
import ru.otus.hw.dao.dto.QuestionDto;
import ru.otus.hw.domain.Answer;
import ru.otus.hw.domain.Question;
import ru.otus.hw.exceptions.QuestionReadException;
import ru.otus.hw.utils.TestFileResourceUtils;

import java.io.Reader;
import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
public class CsvQuestionDao implements QuestionDao {

    private final TestFileNameProvider fileNameProvider;

    private final AnswerDao answerDao;

    @Override
    public List<QuestionDto> findAll() throws QuestionReadException {
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

            questionsReader.close();
        } catch (Exception e) {
            throw  new QuestionReadException("File not found", new Exception());
        }

        return questions;
    }

    @Override
    public List<Question> toListQuestions(List<QuestionDto> questionDtos) {
        if (questionDtos == null || questionDtos.isEmpty()) {
            return null;
        }

        List<Question> questions = new ArrayList<>();

        for (QuestionDto questionDto : questionDtos) {
            List<AnswerDto> answerDtos = questionDto.getAnswers();
            List<Answer> answers = answerDao.toListDomainAnswers(answerDtos);

            questions.add(new Question(questionDto.getText(), answers));
        }

        return questions;
    }
}
