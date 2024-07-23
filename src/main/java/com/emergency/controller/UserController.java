package com.emergency.controller;

import com.emergency.dto.UserAfterCreationDto;
import com.emergency.dto.UserCreateDto;
import com.emergency.entity.User;
import com.emergency.service.UserService;
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

