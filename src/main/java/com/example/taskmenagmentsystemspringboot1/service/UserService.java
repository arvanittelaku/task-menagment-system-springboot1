package com.example.taskmenagmentsystemspringboot1.service;

import com.example.taskmenagmentsystemspringboot1.dtos.user.CreateUserDto;
import com.example.taskmenagmentsystemspringboot1.dtos.user.LoginUserDto;
import com.example.taskmenagmentsystemspringboot1.dtos.user.UserProfileDto;
import com.example.taskmenagmentsystemspringboot1.dtos.user.UserViewDto;
import com.example.taskmenagmentsystemspringboot1.entities.user.User;
import com.example.taskmenagmentsystemspringboot1.entities.user.UserRole;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface UserService {

    void deleteUser(Long id);

    void registerManager(CreateUserDto createUserDto);

    UserViewDto getUserById(Long id);
    List<User> getAllUsersByRole(UserRole role);

}
