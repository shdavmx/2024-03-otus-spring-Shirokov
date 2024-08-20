package ru.otus.hw.commands;

import lombok.Data;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import ru.otus.hw.services.TestRunnerService;

@ShellComponent(value = "Application for test students")
@Data
public class TestCommands {
    private final TestRunnerService testRunnerService;

    @ShellMethod(key = "test", value = "Test student")
    public void runTest() {
        testRunnerService.run();
    }
}
