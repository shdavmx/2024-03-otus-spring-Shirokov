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

    @ShellMethod(value = "Find author by name", key = "abn")
    public String findAuthorByName(String fullName) {
        return authorConverter.authorToString(authorService.findByName(fullName));
    }

    @ShellMethod(value = "Insert new author", key = "ains")
    public String insertNewAuthor(String fullName) {
        return authorConverter.authorToString(authorService.insert(fullName));
    }

    @ShellMethod(value = "Update author", key = "aupd")
    public String updateAuthor(String oldName, String newName) {
        return authorConverter.authorToString(authorService.update(oldName, newName));
    }

    @ShellMethod(value = "Delete author by name", key = "adel")
    public void deleteAuthorByName(String fullName) {
        authorService.deleteByFullName(fullName);
    }
}
