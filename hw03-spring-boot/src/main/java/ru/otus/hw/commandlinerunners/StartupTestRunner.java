package ru.otus.hw.commandlinerunners;

import lombok.Data;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import ru.otus.hw.services.TestRunnerService;

@Component
@Data
public class StartupTestRunner implements CommandLineRunner {
    private final TestRunnerService testRunnerService;

    @Override
    public void run(String... args) throws Exception {
        testRunnerService.run();
    }
}
