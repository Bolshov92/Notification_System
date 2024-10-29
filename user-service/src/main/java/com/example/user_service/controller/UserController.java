package com.example.user_service.controller;

import com.example.user_service.dto.UserAfterCreationDto;
import com.example.user_service.dto.UserCreateDto;
import com.example.user_service.entity.User;
import com.example.user_service.service.UserService;
import com.example.user_service.service.JwtService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@AllArgsConstructor
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;
    private final JwtService jwtService;

    @PostMapping(path = "/create")
    public UserAfterCreationDto createUser(@RequestBody UserCreateDto userCreateDto) {
        return userService.createUser(userCreateDto);
    }

    @GetMapping(path = "/get/{id}")
    public User findBy(@PathVariable("id") String id) {
        return userService.getUserById(id);
    }

    @PostMapping("/authenticate")
    public ResponseEntity<String> authenticate(@RequestBody UserCreateDto userCreateDto) {
        try {
            String token = userService.authenticateUser(userCreateDto.getUserName(), userCreateDto.getPassword());
            return ResponseEntity.ok(token);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid username or password");
        }
    }

}
