package ru.otus.hw.services;

public interface IOService {
    void printException(RuntimeException e);

    void printLine(String line);

    void printFormattedLine(String line, Object ...args);

    void printError(String error);
}
