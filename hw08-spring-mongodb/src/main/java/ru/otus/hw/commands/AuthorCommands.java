package ru.otus.hw.commands;

import lombok.RequiredArgsConstructor;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import ru.otus.hw.converters.AuthorConverter;
import ru.otus.hw.services.AuthorService;

import java.util.stream.Collectors;

@RequiredArgsConstructor
@ShellComponent
public class AuthorCommands {
    private final AuthorService authorService;

    private final AuthorConverter authorConverter;

    @ShellMethod(value = "Find all authors", key = "aa")
    public String findAllAuthors() {
        return authorService.findAll().stream()
                .map(authorConverter::authorToString)
                .collect(Collectors.joining("," + System.lineSeparator()));
    }

    @ShellMethod(value = "Find author by id", key = "abid")
    public String findAuthorByName(String id) {
        return authorConverter.authorToString(authorService.findById(id));
    }

    @ShellMethod(value = "Insert new author", key = "ains")
    public String insertNewAuthor(String fullName) {
        return authorConverter.authorToString(authorService.insert(fullName));
    }

    @ShellMethod(value = "Update author", key = "aupd")
    public String updateAuthor(String id, String name) {
        return authorConverter.authorToString(authorService.update(id, name));
    }

    @ShellMethod(value = "Delete author by id", key = "adel")
    public void deleteAuthorByName(String id) {
        authorService.deleteById(id);
    }
}
