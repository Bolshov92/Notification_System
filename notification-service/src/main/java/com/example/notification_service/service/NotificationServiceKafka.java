package com.example.notification_service.service;

import org.apache.catalina.User;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class NotificationServiceKafka {
    @KafkaListener(topics = "user-topic", groupId = "notification-group")
    public void handleUserCreated(User user) {

        System.out.println("Received user: " + user.getName());
    }
}
