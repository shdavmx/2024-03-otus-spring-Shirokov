package ru.otus.hw.services;

import java.io.PrintStream;

public class StreamsIOService implements IOService {
    private final PrintStream printStream;

    public StreamsIOService(PrintStream printStream) {
        this.printStream = printStream;
    }

    @Override
    public void printLine(String line) {
        printStream.println(line);
    }

    @Override
    public void printFormattedLine(String line, Object ...args) {
        printStream.printf(line + "%n", args);
    }
}
