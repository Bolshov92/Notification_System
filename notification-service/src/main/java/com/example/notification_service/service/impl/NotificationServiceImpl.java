package com.example.notification_service.service.impl;

import com.example.notification_service.entity.Notification;
import com.example.notification_service.repository.NotificationRepository;
import com.example.notification_service.service.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;

@Service
public class NotificationServiceImpl implements NotificationService {

    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    @Autowired
    private NotificationRepository notificationRepository;

    @Override
    public void sendNotification(String to, String message) {

        kafkaTemplate.send("notifications-topic", message);

        Notification notification = new Notification();
        notification.setMessage(message);
        notification.setStatus("Sent");
        notification.setSentAt(new Timestamp(System.currentTimeMillis()));
        notificationRepository.save(notification);
    }
}