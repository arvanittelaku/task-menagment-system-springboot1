package com.example.taskmenagmentsystemspringboot1.service.impl;

import com.example.taskmenagmentsystemspringboot1.dtos.user.*;
import com.example.taskmenagmentsystemspringboot1.entities.user.User;
import com.example.taskmenagmentsystemspringboot1.entities.user.UserRole;
import com.example.taskmenagmentsystemspringboot1.exceptions.EntityNotFoundException;
import com.example.taskmenagmentsystemspringboot1.exceptions.UsernameExistsException;
import com.example.taskmenagmentsystemspringboot1.mappers.TaskMapper;
import com.example.taskmenagmentsystemspringboot1.mappers.UserMapper;
import com.example.taskmenagmentsystemspringboot1.repositories.UserRepository;
import com.example.taskmenagmentsystemspringboot1.service.TaskService;
import com.example.taskmenagmentsystemspringboot1.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final TaskService taskService;
    private final TaskMapper taskMapper;
    private final UserMapper userMapper;  // <-- make this final and injected via constructor

    @Override
    public void registerManager(CreateUserDto createUserDto) {
        String username = createUserDto.getUsername().trim().toLowerCase();
        String email = createUserDto.getEmail().trim().toLowerCase();
        createUserDto.setUsername(username);
        createUserDto.setEmail(email);

        if (userRepository.existsByUsername(username)) {
            throw new UsernameExistsException("Username already exists");
        }

        var user = userMapper.fromCreateToUser(createUserDto);
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        userRepository.save(user);
    }

    @Override
    public UserViewDto getUserById(Long id) {
        return userRepository.findById(id)
                .map(userMapper::fromUserToView)
                .orElse(null);
    }

    @Override
    public List<UserViewDto> getAllUsersByRole(UserRole role) {
        return userMapper.fromEntityToViews(userRepository.findByRole(role));
    }

    @Override
    public UserViewDto findUserByUsername(String username) {
        return userRepository.findByUsername(username)
                .map(userMapper::fromUserToView)
                .orElse(null);
    }

    @Override
    public UserViewDto createUser(CreateUserDto createUserDto) {
        if (userRepository.existsByUsername(createUserDto.getUsername())) {
            throw new UsernameExistsException("Username already exists");
        }

        var user = userMapper.fromCreateToUser(createUserDto);
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        return userMapper.fromUserToView(userRepository.save(user));
    }


    @Override
    public List<UserViewDto> getAllManagers() {
        return userMapper.fromEntityToViews(userRepository.findByRole(UserRole.MANAGER));
    }
    @Override
    public void updateUser(long id, UpdateUserDto userDto) {
        userRepository.findById(id).ifPresentOrElse(existingUser -> {
            existingUser.setUsername(userDto.getUsername());
            existingUser.setEmail(userDto.getEmail());

            if (userDto.getPassword() != null && !userDto.getPassword().isEmpty()) {
                existingUser.setPassword(passwordEncoder.encode(userDto.getPassword()));
            }
            userRepository.save(existingUser);
        }, () -> {
            throw new EntityNotFoundException("User not found with id: " + id);
        });
    }

    @Override
    public User findEntityByUsername(String username) {
        return userRepository.findByUsername(username).orElse(null);
    }

    @Override
    public List<UserViewDto> findAll() {
        return userMapper.fromEntityToViews(userRepository.findAll());
    }

    @Override
    public UserViewDto findById(Long id) {
        return userRepository.findById(id)
                .map(userMapper::fromUserToView)
                .orElse(null);
    }

    @Override
    public void deleteUser(Long id) {
        if (userRepository.existsById(id)) {
            userRepository.deleteById(id);
        } else {
            throw new EntityNotFoundException("User not found with id: " + id);
        }
    }
    @Override
    public UserDto getUserProfileById(Long id) {
        return userRepository.findById(id)
                .map(userMapper::fromUserToDto)
                .orElseThrow(() -> new EntityNotFoundException("User not found"));
    }


}
