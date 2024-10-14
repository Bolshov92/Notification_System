package com.example.notification_service.repository;

import com.example.notification_service.entity.Notification;
import org.springframework.data.jpa.repository.JpaRepository;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;

public interface NotificationRepository extends JpaRepository<Notification, Long> {

    List<Notification> findByNotificationTimeAfterAndStatus(LocalDateTime notificationTime, String status);

    Notification findByEventIdAndContactIdAndNotificationTime(Long eventId, Long contactId, LocalDateTime notificationTime);
}

