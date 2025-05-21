package com.example.taskmenagmentsystemspringboot1.security;

import com.example.taskmenagmentsystemspringboot1.entities.user.User;
import com.example.taskmenagmentsystemspringboot1.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

@RequiredArgsConstructor
public class AppUserDetailsService implements UserDetailsService {
    private final UserRepository repository;
    private final User testUser;
    private final User testManager;
    private final User testAdmin;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // First try to find by username
        var user = repository.findByUsername(username);
        
        if (user.isPresent()) {
            return new AppUserDetails(user.get());
        }
        
        // If not found by username, try by email
        var userEmail = repository.findByEmail(username);
        if (userEmail.isPresent()) {
            return new AppUserDetails(userEmail.get());
        }
        
        // If not found in database, check if it's one of our test users
        if (testUser.getEmail().equals(username)) {
            return new AppUserDetails(testUser);
        }
        if (testManager.getEmail().equals(username)) {
            return new AppUserDetails(testManager);
        }
        if (testAdmin.getEmail().equals(username)) {
            return new AppUserDetails(testAdmin);
        }
        
        throw new UsernameNotFoundException("User with username/email " + username + " not found");
    }
}