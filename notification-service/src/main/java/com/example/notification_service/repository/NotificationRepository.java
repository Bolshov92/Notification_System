package com.example.notification_service.repository;

import com.example.notification_service.entity.Notification;
import org.springframework.data.jpa.repository.JpaRepository;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;

public interface NotificationRepository extends JpaRepository<Notification, Long> {

    List<Notification> findByNotificationTimeAfterAndStatus(LocalDateTime notificationTime, String status);
    Notification findByEventNameAndPhoneNumberAndContactIdAndNotificationTime(String eventName, String phoneNumber, Long contactId, LocalDateTime notificationTime);

    List<Notification> findByNotificationTimeBeforeAndStatus(LocalDateTime now, String pending);

    List<Notification> findByStatus(String pending);
}

