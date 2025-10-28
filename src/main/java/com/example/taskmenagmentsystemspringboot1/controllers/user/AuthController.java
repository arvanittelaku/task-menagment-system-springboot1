package com.example.taskmenagmentsystemspringboot1.controllers.user;

import com.example.taskmenagmentsystemspringboot1.dtos.auth.AuthResponse;
import com.example.taskmenagmentsystemspringboot1.dtos.auth.LoginRequest;
import com.example.taskmenagmentsystemspringboot1.dtos.user.CreateUserDto;
import com.example.taskmenagmentsystemspringboot1.exceptions.UsernameExistsException;
import com.example.taskmenagmentsystemspringboot1.service.AuthenticationService;
import com.example.taskmenagmentsystemspringboot1.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/v1/")
@RequiredArgsConstructor
public class AuthController {
    private final AuthenticationService service;
    private final UserService userService;

    @PostMapping("auth/login")
    @PreAuthorize("permitAll()")
    public ResponseEntity<?> login(@RequestBody @Valid LoginRequest request) {
        try {
            // Step 1: Authenticate user
            var user = service.authenticate(request.getEmail(), request.getPassword());

            // Step 2: Generate token
            var token = service.generateToken(user);
            var authResponse = new AuthResponse(token, 86400000L); // 1 day

            return ResponseEntity.ok(authResponse);
        } catch (Exception e) {
            return ResponseEntity.status(401).body("Invalid email or password");
        }
    }

    @PostMapping("auth/register")
    @PreAuthorize("permitAll()")
    public ResponseEntity<?> register(@RequestBody @Valid CreateUserDto createUserDto) {
        try {
            userService.registerPublicUser(createUserDto);
            return ResponseEntity.status(HttpStatus.CREATED).body(Map.of(
                    "message", "Registration successful! You can now login.",
                    "email", createUserDto.getEmail()));
        } catch (UsernameExistsException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(Map.of("error", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("error", "Registration failed: " + e.getMessage()));
        }
    }

}
