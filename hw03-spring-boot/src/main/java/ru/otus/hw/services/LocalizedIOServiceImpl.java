package ru.otus.hw.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class LocalizedIOServiceImpl implements LocalizedIOService {
    private final LocalizedMessagesService localizedMessagesService;

    private final IOService ioService;

    @Override
    public void printLineLocalized(String code) {
        ioService.printLine(localizedMessagesService.getMessage(code));
    }

    @Override
    public void printFormattedLineLocalized(String code, Object... args) {
        ioService.printLine(localizedMessagesService.getMessage(code, args));
    }

    @Override
    public String readStringWithPromptLocalized(String promptCode) {
        return ioService.readStringWithPrompt(localizedMessagesService.getMessage(promptCode));
    }

    @Override
    public int readIntForRangeLocalized(int min, int max, String errorMessageCode) {
        return ioService.readIntForRange(min, max, localizedMessagesService.getMessage(errorMessageCode));
    }

    @Override
    public int readIntForRangeWithPromptLocalized(int min, int max, String promptCode, String errorMessageCode) {
        return ioService.readIntForRangeWithPrompt(min, max,
                localizedMessagesService.getMessage(promptCode),
                localizedMessagesService.getMessage(errorMessageCode)
                );
    }

    @Override
    public void printLine(String line) {
        ioService.printLine(line);
    }

    @Override
    public void printFormattedLine(String line, Object... args) {
        ioService.printFormattedLine(line, args);
    }

    @Override
    public String readString() {
        return ioService.readString();
    }

    @Override
    public int readInt() {
        return ioService.readInt();
    }

    @Override
    public String readStringWithPrompt(String prompt) {
        return ioService.readStringWithPrompt(prompt);
    }

    @Override
    public int readIntForRange(int min, int max, String errorMessage) {
        return ioService.readIntForRange(min, max, errorMessage);
    }

    @Override
    public int readIntForRangeWithPrompt(int min, int max, String prompt, String errorMessage) {
        return ioService.readIntForRangeWithPrompt(min, max, prompt, errorMessage);
    }
}
