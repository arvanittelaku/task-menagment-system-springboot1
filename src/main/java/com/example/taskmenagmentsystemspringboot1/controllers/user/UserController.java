package com.example.taskmenagmentsystemspringboot1.controllers.user;

import com.example.taskmenagmentsystemspringboot1.dtos.user.CreateUserDto;
import com.example.taskmenagmentsystemspringboot1.dtos.user.UpdateUserDto;
import com.example.taskmenagmentsystemspringboot1.dtos.user.UserDto;
import com.example.taskmenagmentsystemspringboot1.dtos.user.UserViewDto;
import com.example.taskmenagmentsystemspringboot1.entities.user.UserRole;
import com.example.taskmenagmentsystemspringboot1.service.UserService;
import jakarta.validation.Valid;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/users")
public class UserController {
    private final UserService userService;

    @GetMapping
    public ResponseEntity<List<UserViewDto>> findAll() {
        return ResponseEntity.ok(userService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserViewDto> findById(@PathVariable Long id) {
        return ResponseEntity.ok(userService.findById(id));
    }

    @PostMapping("/create")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<UserViewDto> create(@RequestBody @Valid CreateUserDto createUserDto) {
        return ResponseEntity.ok(userService.createUser(createUserDto));
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public ResponseEntity<Void> update(@PathVariable Long id, @RequestBody @Valid UpdateUserDto userDto) {
        userService.updateUser(id, userDto);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/register/manager")
    public ResponseEntity<Void> registerManager(@RequestBody @Valid CreateUserDto createUserDto) {
        userService.registerManager(createUserDto);
        return ResponseEntity.noContent().build();
    }
    @GetMapping("/role/{role}")
    public ResponseEntity<List<UserViewDto>> getUsersByRole(@PathVariable UserRole role) {
        return ResponseEntity.ok(userService.getAllUsersByRole(role));
    }

    @GetMapping("/username/{username}")
    public ResponseEntity<UserViewDto> findByUsername(@PathVariable String username) {
        return ResponseEntity.ok(userService.findUserByUsername(username));
    }
    @GetMapping("/managers")
    public ResponseEntity<List<UserViewDto>> getAllManagers() {
        return ResponseEntity.ok(userService.getAllManagers());
    }

    @GetMapping("/{id}/profile")
    public ResponseEntity<UserDto> getUserProfile(@PathVariable Long id) {
        return ResponseEntity.ok(userService.getUserProfileById(id));
    }




}
