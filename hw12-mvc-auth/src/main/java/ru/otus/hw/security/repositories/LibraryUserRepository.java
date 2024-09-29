package ru.otus.hw.security.repositories;

import org.springframework.data.mongodb.repository.MongoRepository;
import ru.otus.hw.security.models.LibraryUser;

public interface LibraryUserRepository extends MongoRepository<LibraryUser, String> {
    LibraryUser findByUsername(String username);
}
