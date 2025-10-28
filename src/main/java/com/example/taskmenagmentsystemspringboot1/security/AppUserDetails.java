package com.example.taskmenagmentsystemspringboot1.security;

import com.example.taskmenagmentsystemspringboot1.entities.user.User;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
@Setter
@Getter
@RequiredArgsConstructor
public class AppUserDetails implements UserDetails {
    private final User user;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return user.getRole().getAuthorities();
    }

    @Override
    public String getPassword() {
        return user.getPassword();
    }

    @Override
    public String getUsername() {
        return user.getEmail(); // Use email as the principal identifier
    }
    
    public String getActualUsername() {
        return user.getUsername(); // For display purposes
    }

    public long getId() {
        return user.getId();
    }

    public String getRole() {
        return user.getRole().name();
    }
}