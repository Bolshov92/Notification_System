package com.emergency.service.impl;

import com.emergency.service.NotificationService;
import com.emergency.service.SmsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class NotificationServiceImpl implements NotificationService {
    @Autowired
    private SmsService smsService;

    @Override
    public void sendNotification(String to, String message) {
        smsService.sendSms(to, message);
    }
}