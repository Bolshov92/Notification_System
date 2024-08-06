package com.example.Sms_service.controller;

import com.example.Sms_service.dto.SmsRequestDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.example.Sms_service.service.SmsService;

@RestController
@RequestMapping("api/sms")
public class SmsServiceController {

    private final SmsService smsService;

    @Autowired
    public SmsServiceController(SmsService smsService) {
        this.smsService = smsService;
    }

    @PostMapping("/send")
    public ResponseEntity<String> sendSms(@RequestBody SmsRequestDto smsRequestDto) {
        smsService.sendSms(smsRequestDto.getTo(), smsRequestDto.getText());
        return ResponseEntity.ok("SMS sent successfully");
    }
}
