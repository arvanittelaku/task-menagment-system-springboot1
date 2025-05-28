package com.example.taskmenagmentsystemspringboot1.controllers.user;

import com.example.taskmenagmentsystemspringboot1.dtos.auth.AuthResponse;
import com.example.taskmenagmentsystemspringboot1.dtos.auth.LoginRequest;
import com.example.taskmenagmentsystemspringboot1.security.AppUserDetails;
import com.example.taskmenagmentsystemspringboot1.service.AuthenticationService;
import com.example.taskmenagmentsystemspringboot1.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/")
@RequiredArgsConstructor
public class AuthController {
    private final AuthenticationService service;
    private final UserService userService;

    @PostMapping("auth/login")
    @CrossOrigin(origins = "*")
    @PreAuthorize("permitAll()")
    public ResponseEntity<AuthResponse> login(@RequestBody @Valid LoginRequest request) {
        // hapi 1: Authenticate user
        var user = service.authenticate(request.getEmail(), request.getPassword());

        // hapi 2: Generate token
        var token = service.generateToken(user);

        var authResponse = new AuthResponse(token, 86400000L); // 1 day

        return ResponseEntity.ok(authResponse);
    }


}
