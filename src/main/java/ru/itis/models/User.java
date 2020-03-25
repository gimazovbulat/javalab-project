package ru.itis.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.itis.security.Role;

import java.util.HashSet;
import java.util.Set;

@Data
@AllArgsConstructor
@Builder
public class User {
    private Long id;
    private String email;
    private String password;
    private UserState userState;
    private String confirmLink;
    private String avaPath;
    private Set<Role> roles;

    public User() {
        roles = new HashSet<>();
    }
}
