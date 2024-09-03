package ru.otus.hw.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.otus.hw.converters.AuthorToDtoConverter;
import ru.otus.hw.exceptions.EntityNotFoundException;
import ru.otus.hw.models.Author;
import ru.otus.hw.models.dto.AuthorDto;
import ru.otus.hw.repositories.AuthorRepository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class AuthorServiceImpl implements AuthorService {
    private final AuthorRepository authorRepository;

    @Override
    public List<AuthorDto> findAll() {
        return authorRepository.findAll().stream()
                .map(AuthorToDtoConverter::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public AuthorDto findById(long id) {
        Optional<Author> author = authorRepository.findById(id);
        if (author.isPresent()) {
            return AuthorToDtoConverter.toDto(author.get());
        }
        throw new EntityNotFoundException("Author with id %d not found");
    }
}
