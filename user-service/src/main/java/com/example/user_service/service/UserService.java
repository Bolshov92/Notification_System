package com.example.user_service.service;

import com.example.user_service.dto.UserAfterCreationDto;
import com.example.user_service.dto.UserCreateDto;
import com.example.user_service.entity.User;

public interface UserService {
    UserAfterCreationDto createUser(UserCreateDto user);
    User getUserById(String id);
}
