package ru.otus.hw.services;

public interface IOService {

    void printLine(String line);

    void printFormattedLine(String line, Object ...args);

    String readString();

    int readInt();

    String readStringWithPrompt(String prompt);

    int readIntForRange(int min, int max, String errorMessage);

    int readIntForRangeWithPrompt(int min, int max, String prompt, String errorMessage);
}
