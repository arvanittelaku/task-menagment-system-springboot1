package com.example.taskmenagmentsystemspringboot1.dtos.user;

import lombok.Data;

@Data
public class UserDto {
    private Long id;
    private String username;
    private String email;
    private String fullName;
    private String role;
    private Long createdByManagerId;

}

