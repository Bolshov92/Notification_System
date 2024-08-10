package com.example.smsservice.dto;

import lombok.Data;
@Data
public class SmsRequestDto {
    private String to;
    private String text;
}
