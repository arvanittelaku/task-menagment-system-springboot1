package com.example.taskmenagmentsystemspringboot1.repositories;

import com.example.taskmenagmentsystemspringboot1.entities.user.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User,Long> {

    Object findByUsername(String username);

    void removeById(Long id);
}
