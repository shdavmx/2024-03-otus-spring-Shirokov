package ru.otus.hw.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.otus.hw.converters.GenreConverter;
import ru.otus.hw.models.dto.GenreDto;
import ru.otus.hw.repositories.GenreRepository;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class GenreServiceImpl implements GenreService {
    private final GenreRepository genreRepository;

    private final GenreConverter genreConverter;

    @Override
    public List<GenreDto> findAll() {
        return genreRepository.findAll().stream()
                .map(genreConverter::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<GenreDto> findAllByIds(Set<Long> ids) {
        return genreRepository.findAllById(ids).stream()
                .map(genreConverter::toDto)
                .collect(Collectors.toList());
    }
}
