package ru.otus.hw.config;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Repository;

@Repository
@PropertySource("classpath:application.properties")
@Data
public class AppProperties implements TestConfig, TestFileNameProvider {

    @Value("${test.rightAnswersCountToPass}")
    private int rightAnswersCountToPass;

    @Value("${test.testFileName}")
    private String testFileName;
}
