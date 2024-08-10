package com.example.user_service.service.impl;

import com.example.user_service.dto.UserAfterCreationDto;
import com.example.user_service.dto.UserCreateDto;
import com.example.user_service.entity.User;
import com.example.user_service.entity.UserInfo;
import com.example.user_service.mapper.UserMapper;
import com.example.user_service.repository.RoleRepository;
import com.example.user_service.repository.UserInfoRepository;
import com.example.user_service.repository.UserRepository;
import com.example.user_service.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final UserInfoRepository userInfoRepository;
    private final RoleRepository roleRepository;
    private final KafkaTemplate<String, Object> kafkaTemplate;
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

        kafkaTemplate.send("user-topic", savedUser);

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
