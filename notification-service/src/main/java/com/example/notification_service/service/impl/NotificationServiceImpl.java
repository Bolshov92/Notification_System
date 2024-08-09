package com.example.notification_service.service.impl;


import com.example.notification_service.dto.SmsRequest;
import com.example.notification_service.entity.Notification;
import com.example.notification_service.repository.NotificationRepository;
import com.example.notification_service.service.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.sql.Timestamp;

@Service
public class NotificationServiceImpl implements NotificationService {
    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private  NotificationRepository notificationRepository;

    @Override
    public void sendNotification(String to, String message) {
        String smsServiceUrl = "http://api-gateway/sms/send";
        SmsRequest request = new SmsRequest(to, message);
        restTemplate.postForObject(smsServiceUrl, request, Void.class);

        Notification notification = new Notification();
        notification.setMessage(message);
        notification.setStatus("Sent");
        notification.setSentAt(new Timestamp(System.currentTimeMillis()));
        notificationRepository.save(notification);
    }
}