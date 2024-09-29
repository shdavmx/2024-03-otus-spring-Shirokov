package ru.otus.hw.security.models;

import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Document(collection = "library_users")
public class LibraryUser implements UserDetails {
    @Id
    private String id;

    @Size(min = 2, message = "More than 2 chars")
    private String username;

    @Size(min = 2, message = "More than 2 chars")
    private String password;

    private List<LibraryRole> roles;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return getRoles();
    }
}
