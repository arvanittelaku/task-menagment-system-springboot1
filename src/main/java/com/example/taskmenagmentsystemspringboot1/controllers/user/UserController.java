package com.example.taskmenagmentsystemspringboot1.controllers.user;

import com.example.taskmenagmentsystemspringboot1.dtos.user.CreateUserDto;
import com.example.taskmenagmentsystemspringboot1.dtos.user.UpdateUserDto;
import com.example.taskmenagmentsystemspringboot1.dtos.user.UserProfileDto;
import com.example.taskmenagmentsystemspringboot1.dtos.user.UserViewDto;
import com.example.taskmenagmentsystemspringboot1.entities.user.User;
import com.example.taskmenagmentsystemspringboot1.entities.user.UserRole;
import com.example.taskmenagmentsystemspringboot1.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1")
public class UserController {
    private final UserService userService;

    // Only admins can create a user
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public ResponseEntity<Void> createUser(@RequestBody CreateUserDto createUserDto) {
        userService.createUser(createUserDto);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    // Only admins can get all managers
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/managers")
    public ResponseEntity<List<UserViewDto>> getAllManagers() {
        return ResponseEntity.status(HttpStatus.OK).body(userService.getAllManagers());
    }

    // Only admins can delete a user
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@RequestParam Long id) {
        userService.deleteUser(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    // Only admins can register a manager
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/register/manager")
    public ResponseEntity<Void> registerManager(@RequestBody CreateUserDto createUserDto) {
        userService.registerManager(createUserDto);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    // Only admins can get user by ID
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/{id}")
    public ResponseEntity<UserViewDto> getUserById(@PathVariable Long id) {
        return ResponseEntity.status(HttpStatus.OK).body(userService.getUserById(id));
    }

    // Only admins can get users by role
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/by-role")
    public ResponseEntity<List<UserViewDto>> getAllByRole(@RequestParam UserRole role) {
        return ResponseEntity.status(HttpStatus.OK).body(userService.getAllUsersByRole(role));
    }

    // Only admins can find user by username
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/username/{username}")
    public ResponseEntity<UserProfileDto> findUserByUsername(@PathVariable String username) {
        return ResponseEntity.status(HttpStatus.OK).body(userService.findUserByUsername(username));
    }

    // Only admins can update a user
    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping
    public ResponseEntity<UserProfileDto> updateUser(@RequestBody UpdateUserDto userDto) {
        return ResponseEntity.status(HttpStatus.OK).body(userService.updateUser(userDto));
    }

    // Only admins can register a user
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/user/register")
    public ResponseEntity<Void> registerUser(@RequestBody CreateUserDto createUserDto) {
        userService.createUser(createUserDto);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
}
