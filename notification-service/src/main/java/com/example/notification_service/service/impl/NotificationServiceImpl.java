package com.example.notification_service.service.impl;


import com.example.notification_service.dto.SmsRequest;
import com.example.notification_service.service.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class NotificationServiceImpl implements NotificationService {
    @Autowired
    private RestTemplate restTemplate;

    @Override
    public void sendNotification(String to, String message) {
        String smsServiceUrl = "http://api-gateway/sms/send"; // API Gateway URL
        SmsRequest request = new SmsRequest(to, message);
        restTemplate.postForObject(smsServiceUrl, request, Void.class);
    }
}