package com.example.taskmenagmentsystemspringboot1.dtos.user;

import com.example.taskmenagmentsystemspringboot1.entities.user.UserRole;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor

public class CreateUserDto {
    @NotNull(message = "Username is required")
    @NotBlank(message = "Username is required")
    @Size(min = 8,message = "Username must be at least 8 characters long")
    private String username;
    @NotNull(message = "Password is required")
    @NotBlank(message = "Password is required")
    @Size(min = 8,message = "Password must be at least 8 characters long")
    private String password;
    @NotNull(message = "Email is required")
    @NotBlank(message = "Email is required")
    @Email
    private String email;

    @NotNull(message = "Role is required")
    private UserRole role;

}
