package com.example.taskmenagmentsystemspringboot1.service;

import com.example.taskmenagmentsystemspringboot1.dtos.user.CreateUserDto;
import com.example.taskmenagmentsystemspringboot1.dtos.user.LoginUserDto;
import com.example.taskmenagmentsystemspringboot1.dtos.user.UserProfileDto;
import com.example.taskmenagmentsystemspringboot1.dtos.user.UserViewDto;
import com.example.taskmenagmentsystemspringboot1.entities.user.User;
import org.springframework.stereotype.Service;

@Service
public interface UserService {
    UserProfileDto login(LoginUserDto loginUserDto);
    User register(CreateUserDto createUserDto);
    UserViewDto getUser(Long id);
    void deleteUser(Long id);


}
