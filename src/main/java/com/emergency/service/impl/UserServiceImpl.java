package com.emergency.service.impl;

import com.emergency.dto.UserAfterCreationDto;
import com.emergency.dto.UserCreateDto;
import com.emergency.entity.User;
import com.emergency.entity.UserInfo;
import com.emergency.mapper.UserMapper;
import com.emergency.repository.RoleRepository;
import com.emergency.repository.UserInfoRepository;
import com.emergency.repository.UserRepository;
import com.emergency.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final UserInfoRepository userInfoRepository;
    private final RoleRepository roleRepository;
    BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @Override
    public UserAfterCreationDto createUser(UserCreateDto userCreateDto) {
        Optional<User> existingUser = userRepository.findByUserInfoUserName(userCreateDto.getUserName());
        if (existingUser.isPresent()) {
            throw new RuntimeException("User with the same username already exists.");
        }

        UserInfo userInfo = new UserInfo();
        userInfo.setUserName(userCreateDto.getUserName());
        userInfo.setPassword(passwordEncoder.encode(userCreateDto.getPassword()));
        UserInfo savedUserInfo = userInfoRepository.save(userInfo);

        User user = new User();
        user.setUserInfo(savedUserInfo);
        savedUserInfo.setUser(user);
        user.setName(userCreateDto.getUserName());
        user.setEmail(userCreateDto.getEmail());
        user.setPhoneNumber(userCreateDto.getPhoneNumber());
        user.setUserInfo(savedUserInfo);
        User savedUser = userRepository.save(user);
        UserAfterCreationDto userAfterCreationDto = userMapper.toDto(savedUser);
        userAfterCreationDto.setUserId(String.valueOf(savedUser.getId()));

        return userAfterCreationDto;

    }

    @Override
    public User getUserById(String id) {
        User user = userRepository.findUserById(Long.parseLong(id));
        if (user == null) {
            throw new IllegalStateException("User not found");
        }
        return user;
    }
}
