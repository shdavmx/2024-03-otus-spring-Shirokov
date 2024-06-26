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

    public void printException(RuntimeException e) {
        printStream.println("[Error][RuntimeException]");
        printStream.println(e.getMessage());
    }

    public void printError(String error) {
        String errorMessage = String.format("[Error] %s", error);
        printStream.println(errorMessage);
    }
}
