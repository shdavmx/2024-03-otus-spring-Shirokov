package ru.otus.hw.services;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import ru.otus.hw.config.TestConfig;

import java.io.InputStream;
import java.io.PrintStream;
import java.util.Scanner;

@Service
public class StreamsIOService implements IOService {
    private final TestConfig testConfig;

    private final PrintStream printStream;

    private final Scanner scanner;

    public StreamsIOService(@Value("#{T(System).out}") PrintStream printStream,
                            @Value("#{T(System).in}")InputStream inputStream,
                            TestConfig testConfig) {
        this.printStream = printStream;
        this.scanner = new Scanner(inputStream);
        this.testConfig = testConfig;
    }

    @Override
    public void printLine(String line) {
        printStream.println(line);
    }

    @Override
    public void printFormattedLine(String line, Object ...args) {
        printStream.printf(line + "%n", args);
    }

    @Override
    public String readString() {
        return scanner.nextLine();
    }

    @Override
    public int readInt() {
        String strValue = scanner.nextLine();
        return Integer.parseInt(strValue);
    }

    @Override
    public String readStringWithPrompt(String prompt) {
        printLine(prompt);
        return readString();
    }

    @Override
    public int readIntForRange(int min, int max, String errorMessage) {
        for (int i = 0; i < testConfig.getMaxAttemptsInputs(); i++) {
            try {
                int intValue = readInt();
                if (intValue < min || intValue > max) {
                    throw new IllegalArgumentException();
                }
                return intValue;
            } catch (IllegalArgumentException e) {
                printLine(errorMessage);
            }
        }
        throw new IllegalArgumentException("Error during reading int value");
    }

    @Override
    public int readIntForRangeWithPrompt(int min, int max, String prompt, String errorMessage) {
        printLine(prompt);
        return readIntForRange(min, max, errorMessage);
    }
}