package com.example.Sms_service.dto;

import lombok.Data;

@Data
public class SmsRequestDto {
    private String to;
    private String text;
}
