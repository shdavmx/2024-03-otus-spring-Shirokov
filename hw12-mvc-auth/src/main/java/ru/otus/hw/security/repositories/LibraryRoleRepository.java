package ru.otus.hw.security.repositories;

import org.springframework.data.mongodb.repository.MongoRepository;
import ru.otus.hw.security.models.LibraryRole;

public interface LibraryRoleRepository extends MongoRepository<LibraryRole, String> {
}
