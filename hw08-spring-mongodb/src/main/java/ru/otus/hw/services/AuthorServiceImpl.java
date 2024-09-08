package ru.otus.hw.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.otus.hw.converters.AuthorConverter;
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

    private final AuthorConverter authorConverter;

    @Override
    public List<AuthorDto> findAll() {
        return authorRepository.findAll().stream()
                .map(authorConverter::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public AuthorDto findById(String id) {
        Optional<Author> author = authorRepository.findById(id);
        if (author.isPresent()) {
            return authorConverter.toDto(author.get());
        }
        throw new EntityNotFoundException("Author '%s' not found".formatted(id));
    }

    @Override
    public AuthorDto insert(String fullName) {
        return authorConverter.toDto(authorRepository.insert(new Author(fullName)));
    }

    @Override
    public AuthorDto update(String id, String name) {
        return authorConverter.toDto(authorRepository.save(new Author(id, name)));
    }

    @Override
    public void deleteById(String id) {
        AuthorDto authorDto = findById(id);
        Author author = authorConverter.toDbEntry(authorDto);

        authorRepository.delete(author);
    }
}
