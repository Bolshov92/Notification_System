package com.example.notification_service.service;

import com.example.notification_service.repository.NotificationRepository;
import org.springframework.kafka.core.KafkaTemplate;

import java.sql.Timestamp;

public interface NotificationService {
    public void createNotifications(String fileName, String eventName, Timestamp sendTime);
}
