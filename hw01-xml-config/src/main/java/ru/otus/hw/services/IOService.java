package ru.otus.hw.services;

public interface IOService {
    void printLine(String line);

    void printFormattedLine(String line, Object ...args);
}
