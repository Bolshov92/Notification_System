package com.emergency.service;

import com.emergency.dto.UserAfterCreationDto;
import com.emergency.dto.UserCreateDto;
import com.emergency.entity.User;

public interface UserService {
    UserAfterCreationDto createUser(UserCreateDto user);
    User getUserById(String id);
}
