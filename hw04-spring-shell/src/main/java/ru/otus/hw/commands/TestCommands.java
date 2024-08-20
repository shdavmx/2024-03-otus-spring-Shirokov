package ru.otus.hw.commands;

import lombok.RequiredArgsConstructor;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import ru.otus.hw.services.TestRunnerService;

@ShellComponent(value = "Application for test students")
@RequiredArgsConstructor
public class TestCommands {
    private final TestRunnerService testRunnerService;

    @ShellMethod(key = "test", value = "Test student")
    public void runTest() {
        testRunnerService.run();
    }
}
