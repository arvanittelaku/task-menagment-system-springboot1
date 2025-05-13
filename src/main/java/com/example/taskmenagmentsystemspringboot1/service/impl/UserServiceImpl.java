package com.example.taskmenagmentsystemspringboot1.service.impl;

import com.example.taskmenagmentsystemspringboot1.dtos.task.ViewTaskDto;
import com.example.taskmenagmentsystemspringboot1.dtos.user.CreateUserDto;
import com.example.taskmenagmentsystemspringboot1.dtos.user.LoginUserDto;
import com.example.taskmenagmentsystemspringboot1.dtos.user.UserProfileDto;
import com.example.taskmenagmentsystemspringboot1.dtos.user.UserViewDto;
import com.example.taskmenagmentsystemspringboot1.entities.task.Task;
import com.example.taskmenagmentsystemspringboot1.entities.user.User;
import com.example.taskmenagmentsystemspringboot1.entities.user.UserRole;
import com.example.taskmenagmentsystemspringboot1.exceptions.EntityNotFoundException;
import com.example.taskmenagmentsystemspringboot1.exceptions.UsernameExistsException;
import com.example.taskmenagmentsystemspringboot1.mappers.TaskMapper;
import com.example.taskmenagmentsystemspringboot1.mappers.UserMapper;
import com.example.taskmenagmentsystemspringboot1.repositories.UserRepository;
import com.example.taskmenagmentsystemspringboot1.security.JwtUtil;
import com.example.taskmenagmentsystemspringboot1.service.TaskService;
import com.example.taskmenagmentsystemspringboot1.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final TaskService taskService;
    private final TaskMapper taskMapper;
    private  UserMapper userMapper;
    private final JwtUtil jwtUtil; // Inject JwtUtil for token generation



    @Override
    public void registerManager(CreateUserDto createUserDto) {
        var exists = userRepository.findByUsername(createUserDto.getUsername());
        if (exists != null) {
            throw new UsernameExistsException("Username already exists");
        }

        var user = userMapper.fromCreateToUser(createUserDto);

        user.setPassword(passwordEncoder.encode(user.getPassword()));

        userRepository.save(user);
    }


    @Override
    public UserViewDto getUserById(Long id) {
        var exists = userRepository.findById(id);
        if (exists.isPresent()) {
            return userMapper.fromUserToView(exists.get());
        }
        return null;
    }

    @Override
    public List<User> getAllUsersByRole(UserRole role) {
        return userRepository.findByRole(role);
    }

    @Override
    public User findUserByUsername(String username) {
        return userRepository.findByUsername(username).orElse(null);
    }

    @Override
    public void deleteUser(Long id) {
        var exists = userRepository.findById(id);
        if (exists.isPresent()) {
            userRepository.deleteById(id);
        } else {
            throw new EntityNotFoundException("User not found with id: " + id);
        }
    }



    public UserProfileDto login(LoginUserDto loginUserDto) {
        // Find the user by username
        Optional<User> user =  userRepository.findByUsername(loginUserDto.getUsername());

        // Check if the user exists and if the password matches
        if (user == null || !passwordEncoder.matches(loginUserDto.getPassword(), user.get().getPassword())) {
            throw new BadCredentialsException("Invalid credentials");
        }

        // Generate JWT token
        String token = jwtUtil.generateToken(user.get().getUsername());

        // Populate UserProfileDto
        UserProfileDto userProfileDto = new UserProfileDto();
        userProfileDto.setUsername(user.get().getUsername());
        userProfileDto.setEmail(user.get().getEmail());
        userProfileDto.setRole(user.get().getRole());

        // Map tasks to ViewTaskDto
        List<ViewTaskDto> taskDtos = taskMapper.fromTaskToView(taskService.getTasksForCurrentUser(user.get()));
        userProfileDto.setTasks(taskDtos);

        // Set the token
        userProfileDto.setToken(token);

        return userProfileDto;
    }



}
