package com.example.taskmenagmentsystemspringboot1.service.impl;

import com.example.taskmenagmentsystemspringboot1.dtos.user.CreateUserDto;
import com.example.taskmenagmentsystemspringboot1.dtos.user.LoginUserDto;
import com.example.taskmenagmentsystemspringboot1.dtos.user.UserProfileDto;
import com.example.taskmenagmentsystemspringboot1.dtos.user.UserViewDto;
import com.example.taskmenagmentsystemspringboot1.entities.user.User;
import com.example.taskmenagmentsystemspringboot1.entities.user.UserRole;
import com.example.taskmenagmentsystemspringboot1.exceptions.UsernameExistsException;
import com.example.taskmenagmentsystemspringboot1.mappers.UserMapper;
import com.example.taskmenagmentsystemspringboot1.repositories.UserRepository;
import com.example.taskmenagmentsystemspringboot1.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private  UserMapper userMapper;
    @Override
    public UserProfileDto login(LoginUserDto loginUserDto) {
        return null;
    }

    @Override
    public User registerMenager(CreateUserDto createUserDto) {
        var exists = userRepository.findByUsername(createUserDto.getUsername());
        if (exists !=  null) {
            throw new UsernameExistsException("Username already exists");
        }
        var user = userMapper.fromCreateToUser(createUserDto);
        return userRepository.save(user);
    }

    @Override
    public UserViewDto getUserById(Long id) {
        var exists = userRepository.findById(id);
        if (exists.isPresent()) {
            return userMapper.fromUserToView(exists.get());
        }
        return null;
    }

    @Override
    public List<User> getAllUsersMenagers() {
        return userRepository.streamUsersByRole(UserRole.MANAGER);
    }

    @Override
    public void deleteUser(Long id) {
        var exists = userRepository.findById(id);
        if (exists.isPresent()) {
            userRepository.removeById(id);
        }
    }
}
