package com.example.taskmenagmentsystemspringboot1.entities.user;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum Permission {
    ADMIN_READ("admin:read"),
    ADMIN_WRITE("admin:write"), // post, put, delete
    MANAGER_READ("manager:read"),
    MANAGER_WRITE("manager:write"),
    USER_READ("user:read"),
    ;
    @Getter
    private final String permission;
}
