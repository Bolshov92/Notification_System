package com.example.notification_service.repository;

import com.example.notification_service.entity.Notification;
import org.springframework.data.jpa.repository.JpaRepository;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;

public interface NotificationRepository extends JpaRepository<Notification, Long> {
    Notification findByPhoneNumber(String phoneNumber);

    List<Notification> findByNotificationTimeBeforeAndStatus(LocalDateTime notificationTime, String status);
}
