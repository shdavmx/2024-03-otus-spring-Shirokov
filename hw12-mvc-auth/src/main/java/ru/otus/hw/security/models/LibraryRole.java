package ru.otus.hw.security.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.security.core.GrantedAuthority;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Document(collection = "library_role")
public class LibraryRole implements GrantedAuthority {
    @Id
    private String id;

    private String name;

    @DBRef(lazy = true)
    private List<LibraryUser> users;

    @Override
    public String getAuthority() {
        return getName();
    }
}
