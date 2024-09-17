package ru.otus.hw.commands;

import lombok.RequiredArgsConstructor;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import ru.otus.hw.converters.GenreConverter;
import ru.otus.hw.services.GenreService;

import java.util.Set;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@ShellComponent
public class GenreCommands {
    private final GenreService genreService;

    private final GenreConverter genreConverter;

    @ShellMethod(value = "Find all genres", key = "ag")
    public String findAllGenres() {
        return genreService.findAll().stream()
                .map(genreConverter::genreToString)
                .collect(Collectors.joining("," + System.lineSeparator()));
    }

    @ShellMethod(value = "Find all by Ids", key = "agbi")
    public String findAllGenresByIds(Set<String> ids) {
        return genreService.findAllByIds(ids)
                .stream().map(genreConverter::genreToString)
                .collect(Collectors.joining("," + System.lineSeparator()));
    }

    @ShellMethod(value = "Insert new genre", key = "gins")
    public String insertNewGenre(String name) {
        return genreConverter.genreToString(genreService.insert(name));
    }

    @ShellMethod(value = "Update genre", key = "gupd")
    public String updateGenre(String id, String name) {
        return genreConverter.genreToString(genreService.update(id, name));
    }

    @ShellMethod(value = "Delete genre", key = "gdel")
    public void deleteGenre(String id) {
        genreService.deleteById(id);
    }
}
