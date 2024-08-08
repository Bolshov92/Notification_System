package com.example.file_service.service;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "sms-service")
public interface SmsService {
    @PostMapping("/sms/send")
    void sendSms(@RequestParam("phoneNumber") String phoneNumber, @RequestParam("message") String message);
}