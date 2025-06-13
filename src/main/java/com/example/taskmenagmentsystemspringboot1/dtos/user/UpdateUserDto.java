package com.example.taskmenagmentsystemspringboot1.dtos.user;

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
@NoArgsConstructor
@AllArgsConstructor
public class UpdateUserDto {
    private Long id;

    @NotBlank(message = "Username is required")
    @Size(min = 8, message = "Username must be at least 8 characters long")
    private String username;

    @Size(min = 8, message = "Password must be at least 8 characters long")
    private String password;

    @NotBlank(message = "Email is required")
    @Email
    private String email;
}
