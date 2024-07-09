package com.emergency.controller;

import com.emergency.entity.User;
import com.emergency.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
    @RequestMapping("/api/users")
    public class UserController {
        @Autowired
        private UserService userService;

        @PostMapping
        public ResponseEntity<User> createUser(@RequestBody User user){
            User createUser = userService.createUser(user);
            return ResponseEntity.status(HttpStatus.CREATED).body(createUser);
        }

        @GetMapping
    public ResponseEntity<List<User>> getAllUsers(){
            List<User> users = userService.getAllUsers();
            return ResponseEntity.ok(users);
        }
    }

