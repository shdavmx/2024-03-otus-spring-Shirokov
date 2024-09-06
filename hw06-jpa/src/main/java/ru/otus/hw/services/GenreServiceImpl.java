package ru.otus.hw.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.otus.hw.converters.GenreToDtoConverter;
import ru.otus.hw.models.dto.GenreDto;
import ru.otus.hw.repositories.GenreRepository;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class GenreServiceImpl implements GenreService {
    private final GenreRepository genreRepository;

    @Override
    public List<GenreDto> findAll() {
        return genreRepository.findAll().stream()
                .map(GenreToDtoConverter::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<GenreDto> findAllByIds(Set<Long> ids) {
        return genreRepository.findAllByIds(ids).stream()
                .map(GenreToDtoConverter::toDto)
                .collect(Collectors.toList());
    }
}
