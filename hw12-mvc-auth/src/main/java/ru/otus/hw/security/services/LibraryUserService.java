package ru.otus.hw.security.services;

import org.springframework.security.core.userdetails.UserDetailsService;
import ru.otus.hw.security.models.LibraryUser;

public interface LibraryUserService extends UserDetailsService {
    boolean saveUser(LibraryUser user);
}
