package com.emergency.dto;

import lombok.Value;

@Value
public class UserCreateDto {
    String name;
    String email;
    String phoneNumber;
    String userName;
    String password;

}
