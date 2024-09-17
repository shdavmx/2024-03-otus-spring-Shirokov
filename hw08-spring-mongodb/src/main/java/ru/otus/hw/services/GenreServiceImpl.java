package ru.otus.hw.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.otus.hw.converters.GenreConverter;
import ru.otus.hw.exceptions.EntityNotFoundException;
import ru.otus.hw.models.Genre;
import ru.otus.hw.models.dto.GenreDto;
import ru.otus.hw.repositories.BookRepositoryCustom;
import ru.otus.hw.repositories.GenreRepository;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@RequiredArgsConstructor
@Service
public class GenreServiceImpl implements GenreService {
    private final GenreRepository genreRepository;

    private final GenreConverter genreConverter;

    private final BookRepositoryCustom bookRepositoryCustom;

    @Override
    public GenreDto findById(String id) {
        Optional<Genre> genre = genreRepository.findById(id);
        if (genre.isPresent()) {
            return genreConverter.toDto(genre.get());
        }
        throw new EntityNotFoundException("Genre '%s' not found".formatted(id));
    }

    @Override
    public List<GenreDto> findAllByIds(Set<String> ids) {
        return genreRepository.findAllById(ids).stream()
                .map(genreConverter::toDto)
                .toList();
    }

    @Override
    public List<GenreDto> findAll() {
        return genreRepository.findAll().stream()
                .map(genreConverter::toDto)
                .toList();
    }

    @Override
    public GenreDto insert(String name) {
        return genreConverter.toDto(genreRepository.insert(new Genre(name)));
    }

    @Override
    public GenreDto update(String id, String name) {
        return genreConverter.toDto(genreRepository.save(new Genre(id, name)));
    }

    @Override
    public void deleteById(String id) {
        if (!genreRepository.existsById(id)) {
            throw new EntityNotFoundException("Genre with id '%s' not found".formatted(id));
        }

        genreRepository.deleteById(id);
        bookRepositoryCustom.removeGenreArrayElementsById(id);
    }
}
