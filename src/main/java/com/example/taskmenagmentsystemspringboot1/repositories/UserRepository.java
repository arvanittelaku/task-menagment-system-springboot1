package com.example.taskmenagmentsystemspringboot1.repositories;

import com.example.taskmenagmentsystemspringboot1.entities.user.User;
import com.example.taskmenagmentsystemspringboot1.entities.user.UserRole;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User,Long> {

    Optional<User> findByUsername(String username);
    Optional<User> findByEmail(String email);
    List<User> findByRole(UserRole role);

    boolean existsByUsername(String username);

    List<User> findByCreatedByManager(User createdByManager);

    List<User> findByRoleAndCreatedByManager(UserRole role, User createdByManager);
}
