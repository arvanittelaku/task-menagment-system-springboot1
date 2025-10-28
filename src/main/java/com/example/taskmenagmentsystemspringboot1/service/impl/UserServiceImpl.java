package com.example.taskmenagmentsystemspringboot1.service.impl;

import com.example.taskmenagmentsystemspringboot1.dtos.user.ChangePasswordDto;
import com.example.taskmenagmentsystemspringboot1.dtos.user.CreateUserDto;
import com.example.taskmenagmentsystemspringboot1.dtos.user.UpdateProfileDto;
import com.example.taskmenagmentsystemspringboot1.dtos.user.UpdateUserDto;
import com.example.taskmenagmentsystemspringboot1.dtos.user.UserDto;
import com.example.taskmenagmentsystemspringboot1.dtos.user.UserViewDto;
import com.example.taskmenagmentsystemspringboot1.entities.user.User;
import com.example.taskmenagmentsystemspringboot1.entities.user.UserRole;
import com.example.taskmenagmentsystemspringboot1.exceptions.EntityNotFoundException;
import com.example.taskmenagmentsystemspringboot1.exceptions.UsernameExistsException;
import com.example.taskmenagmentsystemspringboot1.mappers.UserMapper;
import com.example.taskmenagmentsystemspringboot1.repositories.UserRepository;
import com.example.taskmenagmentsystemspringboot1.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserMapper userMapper;

    @Override
    public void registerPublicUser(CreateUserDto createUserDto) {
        // Check if username or email already exists
        if (userRepository.existsByUsername(createUserDto.getUsername())) {
            throw new UsernameExistsException("Username already exists");
        }

        if (userRepository.findByEmail(createUserDto.getEmail()).isPresent()) {
            throw new UsernameExistsException("Email already exists");
        }

        var user = userMapper.fromCreateToUser(createUserDto);
        user.setRole(UserRole.USER); // Always register as USER
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        userRepository.save(user);
    }

    @Override
    public void registerManager(CreateUserDto createUserDto) {
        if (userRepository.existsByUsername(createUserDto.getUsername())) {
            throw new UsernameExistsException("Username already exists");
        }

        var user = userMapper.fromCreateToUser(createUserDto);
        user.setRole(UserRole.MANAGER);
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        userRepository.save(user);
    }

    @Override
    public void createUser(CreateUserDto createUserDto, String creatorEmail) {
        if (userRepository.existsByUsername(createUserDto.getUsername())) {
            throw new UsernameExistsException("Username already exists");
        }

        var user = userMapper.fromCreateToUser(createUserDto);
        user.setRole(UserRole.USER);
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        // Use the passed creatorEmail to find the creator and set it
        User creatorUser = userRepository.findByEmail(creatorEmail)
                .orElseThrow(() -> new EntityNotFoundException("Creator user not found!"));

        if (creatorUser.getRole() == UserRole.MANAGER) {
            user.setCreatedByManager(creatorUser);
        }
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
    public UserViewDto findUserByUsername(String email) {
        // Note: Despite method name, this now accepts email (principal identifier)
        return userRepository.findByEmail(email)
                .map(userMapper::fromUserToView)
                .orElse(null);
    }

    @Override
    public List<UserViewDto> getAllManagers() {
        return userMapper.fromEntityToViews(userRepository.findByRole(UserRole.MANAGER));
    }

    @Override
    public List<UserViewDto> findAllUsersBasedOnRole() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            return Collections.emptyList();
        }

        String currentEmail = authentication.getName(); // Now returns email
        User currentUser = userRepository.findByEmail(currentEmail)
                .orElseThrow(() -> new EntityNotFoundException("Authenticated user not found!"));

        if (currentUser.getRole() == UserRole.ADMIN) {
            return userMapper.fromEntityToViews(userRepository.findAll());
        } else if (currentUser.getRole() == UserRole.MANAGER) {
            return userMapper.fromEntityToViews(userRepository.findByCreatedByManager(currentUser));
        } else {
            return Collections.emptyList();
        }
    }

    @Override
    public List<UserViewDto> getAllUsersBasedOnRole(UserRole targetRole) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            return Collections.emptyList();
        }

        String currentEmail = authentication.getName(); // Now returns email
        User currentUser = userRepository.findByEmail(currentEmail)
                .orElseThrow(() -> new EntityNotFoundException("Authenticated user not found!"));

        if (currentUser.getRole() == UserRole.ADMIN) {
            return userMapper.fromEntityToViews(userRepository.findByRole(targetRole));
        } else if (currentUser.getRole() == UserRole.MANAGER) {
            return userMapper.fromEntityToViews(userRepository.findByRoleAndCreatedByManager(targetRole, currentUser));
        } else {
            return Collections.emptyList();
        }
    }

    @Override
    public void updateUser(long id, UpdateUserDto userDto) {
        userRepository.findById(id).ifPresentOrElse(existingUser -> {
            existingUser.setUsername(userDto.getUsername());
            existingUser.setEmail(userDto.getEmail());
            String newPassword = userDto.getPassword();
            if (newPassword != null && !newPassword.trim().isEmpty()) {
                existingUser.setPassword(passwordEncoder.encode(newPassword));
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

    @Override
    public Optional<UserDto> findByUsername(String username) {
        Optional<User> user = userRepository.findByUsername(username);
        return user.map(userMapper::fromUserToDto);
    }

    @Override
    public List<UserViewDto> findAll() {
        return userMapper.fromEntityToViews(userRepository.findAll());
    }

    @Override
    public UserViewDto findById(Long id) {
        return userMapper.fromUserToView(userRepository.findById(id).orElse(null));
    }

    @Override
    public UserDto getProfile(Long userId) {
        return userRepository.findById(userId)
                .map(userMapper::fromUserToDto)
                .orElseThrow(() -> new EntityNotFoundException("User not found"));
    }

    @Override
    public void updateProfile(Long userId, UpdateProfileDto updateProfileDto) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("User not found"));

        // Check if the new username is already taken by another user
        if (!user.getUsername().equals(updateProfileDto.getUsername()) &&
                userRepository.existsByUsername(updateProfileDto.getUsername())) {
            throw new UsernameExistsException("Username already exists");
        }

        // Check if the new email is already taken by another user
        if (!user.getEmail().equals(updateProfileDto.getEmail()) &&
                userRepository.findByEmail(updateProfileDto.getEmail()).isPresent()) {
            throw new UsernameExistsException("Email already exists");
        }

        user.setUsername(updateProfileDto.getUsername());
        user.setEmail(updateProfileDto.getEmail());
        userRepository.save(user);
    }

    @Override
    public void changePassword(Long userId, ChangePasswordDto changePasswordDto) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("User not found"));

        // Verify current password
        if (!passwordEncoder.matches(changePasswordDto.getCurrentPassword(), user.getPassword())) {
            throw new IllegalArgumentException("Current password is incorrect");
        }

        // Verify new passwords match
        if (!changePasswordDto.getNewPassword().equals(changePasswordDto.getConfirmPassword())) {
            throw new IllegalArgumentException("New passwords do not match");
        }

        // Update password
        user.setPassword(passwordEncoder.encode(changePasswordDto.getNewPassword()));
        userRepository.save(user);
    }
}
