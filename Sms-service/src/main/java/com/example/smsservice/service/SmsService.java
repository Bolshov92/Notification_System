package com.example.smsservice.service;

public interface SmsService {

    void sendSms(Long notificationId, String to, String text);
}
