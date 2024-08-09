package com.example.user_service.dto;

import lombok.Data;
import lombok.Value;

@Data
public class UserCreateDto {
    String name;
    String email;
    String phoneNumber;
    String userName;
    String password;

}
