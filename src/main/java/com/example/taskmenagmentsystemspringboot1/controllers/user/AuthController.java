package com.example.taskmenagmentsystemspringboot1.controllers.user;

import com.example.taskmenagmentsystemspringboot1.dtos.auth.LoginRequest;
import com.example.taskmenagmentsystemspringboot1.security.AppUserDetails;
import com.example.taskmenagmentsystemspringboot1.service.AuthenticationService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/v1") // put /auth here to keep it clean
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class AuthController {

    private static final Logger logger = LoggerFactory.getLogger(AuthController.class);

    private final AuthenticationService authenticationService;

    @PostMapping("/auth/login")
    public ResponseEntity<?> login(@Valid @RequestBody LoginRequest request) {
        try {
            UserDetails userDetails = authenticationService.authenticate(request.getEmail(), request.getPassword());
            String token = authenticationService.generateToken(userDetails);

            if (!(userDetails instanceof AppUserDetails)) {
                logger.error("UserDetails is not of expected type for user: {}", request.getEmail());
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .body(Map.of("error", "Internal server error: Unexpected user details type."));
            }
            AppUserDetails appUser = (AppUserDetails) userDetails;

            Map<String, Object> response = new HashMap<>();
            response.put("token", token);
            response.put("username", appUser.getUsername());
            response.put("role", appUser.getRole());
            response.put("userId", appUser.getId());

            return ResponseEntity.ok(response);

        } catch (AuthenticationException e) { // Catch specific authentication failures
            logger.warn("Authentication failed for user {}: {}", request.getEmail(), e.getMessage());
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("error", "Invalid email or password"));
        } catch (StackOverflowError e) { // Explicitly catch StackOverflowError
            logger.error("StackOverflowError during login for user {}: {}", request.getEmail(), e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "An internal server error occurred due to deep recursion. Contact support."));
        } catch (Exception e) { // Catch any other unexpected errors
            logger.error("An unexpected error occurred during login for user {}", request.getEmail(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "An internal server error occurred. Please try again later."));
        }
    }
}
