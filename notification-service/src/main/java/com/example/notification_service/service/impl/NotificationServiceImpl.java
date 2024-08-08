package com.example.notification_service.service.impl;


import com.example.Sms_service.service.SmsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class NotificationServiceImpl implements com.emergency.service.NotificationService {
    @Autowired
    private SmsService smsService;

    @Override
    public void sendNotification(String to, String message) {
        smsService.sendSms(to, message);
    }
}