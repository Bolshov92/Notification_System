package com.example.user_service.controller;

import com.example.user_service.dto.UserAfterCreationDto;
import com.example.user_service.dto.UserCreateDto;
import com.example.user_service.entity.User;
import com.example.user_service.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;


@RestController
@AllArgsConstructor
@RequestMapping("/api/users")
public class UserController {

    private UserService userService;

    @PostMapping(path = "/create")
    public UserAfterCreationDto createUser(@RequestBody UserCreateDto userCreateDto) {
        return userService.createUser(userCreateDto);
    }

    @GetMapping(path = "/get/{id}")
    public User findBy(@PathVariable("id") String id) {
        return userService.getUserById(id);
    }
}

