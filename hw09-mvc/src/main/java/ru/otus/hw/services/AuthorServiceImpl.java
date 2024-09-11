package ru.otus.hw.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.otus.hw.exceptions.EntityNotFoundException;
import ru.otus.hw.models.Author;
import ru.otus.hw.models.dto.AuthorDto;
import ru.otus.hw.repositories.AuthorRepository;
import ru.otus.hw.repositories.BookRepository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class AuthorServiceImpl implements AuthorService {
    private final AuthorRepository authorRepository;

    private final BookRepository bookRepository;

    @Override
    public List<AuthorDto> findAll() {
        return authorRepository.findAll().stream()
                .map(AuthorDto::new)
                .collect(Collectors.toList());
    }

    @Override
    public AuthorDto findById(String id) {
        Optional<Author> author = authorRepository.findById(id);
        if (author.isPresent()) {
            return new AuthorDto(author.get());
        }
        throw new EntityNotFoundException("Author '%s' not found".formatted(id));
    }

    @Override
    public AuthorDto insert(String fullName) {
        return new AuthorDto(authorRepository.insert(new Author(fullName)));
    }

    @Override
    public AuthorDto update(String id, String name) {
        return new AuthorDto(authorRepository.save(new Author(id, name)));
    }

    @Override
    public void deleteById(String id) {
        if (!authorRepository.existsById(id)) {
            throw new EntityNotFoundException("Author with id '%s' not found".formatted(id));
        }

        authorRepository.deleteById(id);
        bookRepository.deleteAllByAuthorId(id);
    }
}
