package com.example.taskmenagmentsystemspringboot1.dtos.user;

import com.example.taskmenagmentsystemspringboot1.entities.user.UserRole;
import jakarta.persistence.Id;
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
public class UserViewDto {

    @Id
    private long id;
    @NotNull(message = "Username is required")
    @NotBlank(message = "Username is required")
    @Size(min = 8, message = "Username must be at least 8 characters long")
    private String username;

    @NotBlank(message = "Email is required")
    @NotNull(message = "Email is required")
    @Email
    private String email;

}
