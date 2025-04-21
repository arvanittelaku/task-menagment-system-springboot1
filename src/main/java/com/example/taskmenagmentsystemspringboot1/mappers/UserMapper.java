package com.example.taskmenagmentsystemspringboot1.mappers;

import com.example.taskmenagmentsystemspringboot1.dtos.user.CreateUserDto;
import com.example.taskmenagmentsystemspringboot1.dtos.user.UpdateUserDto;
import com.example.taskmenagmentsystemspringboot1.dtos.user.UserProfileDto;
import com.example.taskmenagmentsystemspringboot1.dtos.user.UserViewDto;
import com.example.taskmenagmentsystemspringboot1.entities.user.User;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper {
    User fromCreateToUser(CreateUserDto createUserDto);
    User fromUpdateToUser(UpdateUserDto updateUserDto);
    UserViewDto fromUserToView(User user);
    UserProfileDto fromUserToProfile(User user);

}
