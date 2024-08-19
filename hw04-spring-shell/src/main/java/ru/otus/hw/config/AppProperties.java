package ru.otus.hw.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.Locale;
import java.util.Map;

@ConfigurationProperties(prefix = "test")
@Data
@Component
public class AppProperties implements TestConfig, TestFileNameProvider, LocaleConfig, InputConfig {
    private int maxAttemptsInputs;

    private int rightAnswersCountToPass;

    private Locale locale;

    private Map<String, String> fileNameByLocaleTag;

    public void setLocale(String tag) {
        this.locale = Locale.forLanguageTag(tag);
    }

    @Override
    public String getTestFileName() {
        return fileNameByLocaleTag.get(locale.toLanguageTag());
    }
}
