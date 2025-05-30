package com.example.taskmenagmentsystemspringboot1.entities.user;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Collections;
import java.util.List;
import java.util.Set;

import static com.example.taskmenagmentsystemspringboot1.entities.user.Permission.*;

@RequiredArgsConstructor
public enum UserRole {
    ADMIN(Set.of(ADMIN_READ, ADMIN_WRITE, MANAGER_READ, MANAGER_WRITE)),
    MANAGER(Set.of(MANAGER_WRITE, MANAGER_READ)),
    USER(Collections.emptySet());

    @Getter
    private final Set<Permission> permissions;

    public List<SimpleGrantedAuthority> getAuthorities() {
        var authority = new java.util.ArrayList<>(permissions.stream()
                .map(permission -> new SimpleGrantedAuthority(permission.getPermission()))
                .toList());

        authority.add(new SimpleGrantedAuthority("ROLE_" + this.name()));
        return authority;
    }
}
