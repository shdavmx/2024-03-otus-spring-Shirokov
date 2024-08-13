package ru.otus.hw.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.otus.hw.domain.Student;

@Service
@RequiredArgsConstructor
public class StudentServiceImpl implements StudentService {

    private final IOService ioService;

    @Override
    public Student determineCurrentStudent() {
        String firstName = ioService.readStringWithPrompt("Please enter your first name");
        String lastName = ioService.readStringWithPrompt("Please enter your last name");
        return new Student(firstName, lastName);
    }
}
