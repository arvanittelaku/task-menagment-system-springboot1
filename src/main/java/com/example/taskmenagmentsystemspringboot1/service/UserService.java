package com.example.taskmenagmentsystemspringboot1.service;

import com.example.taskmenagmentsystemspringboot1.dtos.user.*;
import com.example.taskmenagmentsystemspringboot1.entities.user.User;
import com.example.taskmenagmentsystemspringboot1.entities.user.UserRole;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface UserService {

    void deleteUser(Long id);

    void registerManager(CreateUserDto createUserDto);

    UserViewDto getUserById(Long id);
    List<UserViewDto> getAllUsersByRole(UserRole role);
    UserProfileDto findUserByUsername(String username);
    void createUser(CreateUserDto createUserDto);
    List<UserViewDto> getAllManagers();
    UserProfileDto updateUser(UpdateUserDto userDto);
}
