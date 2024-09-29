package ru.otus.hw.security.services;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import ru.otus.hw.security.models.LibraryRole;
import ru.otus.hw.security.models.LibraryUser;
import ru.otus.hw.security.repositories.LibraryUserRepository;

import java.util.List;

@RequiredArgsConstructor
@Service
public class LibraryUserServiceImpl implements LibraryUserService {
    private final LibraryUserRepository userRepository;

    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        LibraryUser user = userRepository.findByUsername(username);
        if (user == null) {
            throw new UsernameNotFoundException("User '%s' not found".formatted(username));
        }

        return user;
    }

    @Override
    public boolean saveUser(LibraryUser user) {
        LibraryUser existingUser = userRepository.findByUsername(user.getUsername());
        if (existingUser != null) {
            return false;
        }

        if (user.getRoles() == null || user.getRoles().isEmpty()) {
            user.setRoles(List.of(new LibraryRole("1", "ROLE_LIBUSER", null)));
        }
        user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));

        userRepository.save(user);

        return true;
    }
}
