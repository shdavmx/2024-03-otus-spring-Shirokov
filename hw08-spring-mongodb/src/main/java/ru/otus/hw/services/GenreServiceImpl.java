package ru.otus.hw.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.otus.hw.converters.GenreConverter;
import ru.otus.hw.exceptions.EntityNotFoundException;
import ru.otus.hw.models.Genre;
import ru.otus.hw.models.dto.GenreDto;
import ru.otus.hw.repositories.GenreRepository;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class GenreServiceImpl implements GenreService {
    private final GenreRepository genreRepository;

    private final GenreConverter genreConverter;

    @Override
    public GenreDto findByName(String name) {
        Genre genre = genreRepository.findByName(name);
        if (genre != null) {
            return genreConverter.toDto(genre);
        }
        throw new EntityNotFoundException("Genre '%s' not found".formatted(name));
    }

    @Override
    public List<GenreDto> findAll() {
        return genreRepository.findAll().stream()
                .map(genreConverter::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public GenreDto insert(String name) {
        return genreConverter.toDto(genreRepository.insert(new Genre(name)));
    }

    @Override
    public GenreDto update(String oldName, String newName) {
        GenreDto genreDto = findByName(oldName);
        genreDto.setName(newName);
        Genre genre = genreConverter.toDbEntry(genreDto);

        return genreConverter.toDto(genreRepository.save(genre));
    }

    @Override
    public void deleteByName(String name) {
        GenreDto genreDto = findByName(name);
        Genre genre = genreConverter.toDbEntry(genreDto);

        genreRepository.delete(genre);
    }
}
