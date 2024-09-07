package ru.otus.hw.services;

import ru.otus.hw.models.dto.AuthorDto;

import java.util.List;

public interface AuthorService {
    List<AuthorDto> findAll();

    AuthorDto findByName(String fullName);

    AuthorDto insert(String fullName);

    AuthorDto update(String oldFullName, String newFullName);

    void deleteByFullName(String fullName);
}
