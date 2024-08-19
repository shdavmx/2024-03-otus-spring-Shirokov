package ru.otus.hw.services;

import ru.otus.hw.domain.Student;
import ru.otus.hw.domain.TestResult;

public interface TestService {
    TestResult executeTestFor(Student student);
}
