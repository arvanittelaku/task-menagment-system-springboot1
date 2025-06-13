package com.example.taskmenagmentsystemspringboot1.mappers;

import com.example.taskmenagmentsystemspringboot1.dtos.user.*;
import com.example.taskmenagmentsystemspringboot1.entities.user.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.util.List;

@Mapper(componentModel = "spring")
public interface UserMapper {

    User fromCreateToUser(CreateUserDto createUserDto);
    User fromUpdateToUser(UpdateUserDto updateUserDto);

    @Named("mapToUserDto")
    UserDto fromUserToDto(User user);

    UserDto fromUserToProfile(User user);

    List<UserViewDto> fromEntityToViews(List<User> users);
    @Mapping(target = "createdByManagerId", source = "createdByManager.id")
    UserViewDto fromUserToView(User user);
}
