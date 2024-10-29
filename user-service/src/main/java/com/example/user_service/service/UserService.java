package com.example.user_service.service;

import com.example.user_service.dto.UserAfterCreationDto;
import com.example.user_service.dto.UserCreateDto;
import com.example.user_service.entity.User;

import java.util.Optional;

public interface UserService {
    UserAfterCreationDto createUser(UserCreateDto user);

    User getUserById(String id);

    Optional<User> findUserByUsername(String userName);

    String authenticateUser(String userName, String password);
}
