package com.example.taskmenagmentsystemspringboot1.service;

import com.example.taskmenagmentsystemspringboot1.dtos.user.CreateUserDto;
import com.example.taskmenagmentsystemspringboot1.dtos.user.UpdateUserDto;
import com.example.taskmenagmentsystemspringboot1.dtos.user.UserDto;
import com.example.taskmenagmentsystemspringboot1.dtos.user.UserViewDto;
import com.example.taskmenagmentsystemspringboot1.entities.user.User;
import com.example.taskmenagmentsystemspringboot1.entities.user.UserRole;

import java.util.List;
import java.util.Optional;

public interface UserService {
    void registerManager(CreateUserDto createUserDto);

    void createUser(CreateUserDto createUserDto, String creatorUsername);

    UserViewDto getUserById(Long id);

    List<UserViewDto> getAllUsersByRole(UserRole role);

    UserViewDto findUserByUsername(String username);

    List<UserViewDto> getAllManagers();

    void updateUser(long id, UpdateUserDto userDto);

    User findEntityByUsername(String username);

    List<UserViewDto> findAll();

    UserViewDto findById(Long id);

    void deleteUser(Long id);

    UserDto getUserProfileById(Long id);
    Optional<UserDto> findByUsername(String username);
    List<UserViewDto> findAllUsersBasedOnRole();
    List<UserViewDto> getAllUsersBasedOnRole(UserRole role);
}
