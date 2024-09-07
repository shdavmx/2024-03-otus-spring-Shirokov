package ru.otus.hw.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.otus.hw.converters.AuthorConverter;
import ru.otus.hw.exceptions.EntityNotFoundException;
import ru.otus.hw.models.Author;
import ru.otus.hw.models.dto.AuthorDto;
import ru.otus.hw.repositories.AuthorRepository;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class AuthorServiceImpl implements AuthorService {
    private final AuthorRepository authorRepository;

    private final AuthorConverter authorConverter;

    @Override
    public List<AuthorDto> findAll() {
        return authorRepository.findAll().stream()
                .map(authorConverter::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public AuthorDto findByName(String fullName) {
        Author author = authorRepository.findByFullName(fullName);
        if (author != null) {
            return authorConverter.toDto(author);
        }
        throw new EntityNotFoundException("Author '%s' not found".formatted(fullName));
    }

    @Override
    public AuthorDto insert(String fullName) {
        return authorConverter.toDto(authorRepository.insert(new Author(fullName)));
    }

    @Override
    public AuthorDto update(String oldFullName, String newFullName) {
        AuthorDto authorDto = findByName(oldFullName);
        authorDto.setFullName(newFullName);
        Author author = authorConverter.toDbEntry(authorDto);

        return authorConverter.toDto(authorRepository.save(author));
    }

    @Override
    public void deleteByFullName(String fullName) {
        AuthorDto authorDto = findByName(fullName);
        Author author = authorConverter.toDbEntry(authorDto);

        authorRepository.delete(author);
    }
}
