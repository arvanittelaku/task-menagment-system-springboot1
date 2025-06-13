package com.example.taskmenagmentsystemspringboot1.controllers.user;

import com.example.taskmenagmentsystemspringboot1.dtos.user.CreateUserDto;
import com.example.taskmenagmentsystemspringboot1.dtos.user.UpdateUserDto;
import com.example.taskmenagmentsystemspringboot1.dtos.user.UserDto;
import com.example.taskmenagmentsystemspringboot1.dtos.user.UserViewDto;
import com.example.taskmenagmentsystemspringboot1.entities.user.User;
import com.example.taskmenagmentsystemspringboot1.entities.user.UserRole;
import com.example.taskmenagmentsystemspringboot1.exceptions.ResourceNotFoundException;
import com.example.taskmenagmentsystemspringboot1.mappers.UserMapper;
import com.example.taskmenagmentsystemspringboot1.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@CrossOrigin(origins = "http://localhost:5173")
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/users")
public class UserController {
    private final UserService userService;
    private final UserMapper userMapper;

    @GetMapping
    public ResponseEntity<List<UserViewDto>> findAll() {
        return ResponseEntity.ok(userService.findAllUsersBasedOnRole());
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserViewDto> findById(@PathVariable Long id) {
        UserViewDto user = userService.findById(id);
        if (user == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(user);
    }


    @GetMapping("/me")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<UserViewDto> getCurrentUserProfile(@AuthenticationPrincipal UserDetails principal) {
        return ResponseEntity.ok(userService.findUserByUsername(principal.getUsername()));
    }

    @PostMapping("/register/manager")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> registerManager(@RequestBody @Valid CreateUserDto createUserDto) {
        userService.registerManager(createUserDto);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/register/user")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    public ResponseEntity<Void> registerUser(@RequestBody @Valid CreateUserDto createUserDto,
                                             @AuthenticationPrincipal UserDetails principal) {
        userService.createUser(createUserDto, principal.getUsername());
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/by-role")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    public ResponseEntity<List<UserViewDto>> getUsersByRole(@RequestParam UserRole role) {
        return ResponseEntity.ok(userService.getAllUsersBasedOnRole(role));
    }

    @GetMapping("/managers")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<UserViewDto>> getAllManagers() {
        return ResponseEntity.ok(userService.getAllManagers());
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    public ResponseEntity<String> update(@PathVariable Long id, @RequestBody @Valid UpdateUserDto userDto) {
        userService.updateUser(id, userDto);
        return ResponseEntity.ok("User updated successfully");
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }
    @GetMapping("/by-username")
    public ResponseEntity<UserDto> getUserByUsername(@RequestParam String username) {
        var userOpt = userService.findByUsername(username.trim());
        if (userOpt.isEmpty()) {
            throw new ResourceNotFoundException("User not found with username: " + username);
        }
        return ResponseEntity.ok(userOpt.get());
    }

}
