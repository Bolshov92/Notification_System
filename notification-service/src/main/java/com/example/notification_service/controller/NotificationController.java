package com.example.notification_service.controller;

import com.example.notification_service.dto.NotificationRequestDto;
import com.example.notification_service.service.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.sql.Timestamp;

@RestController
@RequestMapping("/api/notifications")
public class NotificationController {
    @Autowired
    private  NotificationService notificationService;

    @Autowired
    public NotificationController(NotificationService notificationService) {
        this.notificationService = notificationService;
    }

    @PostMapping("/create")
    public ResponseEntity<String> createNotifications(@RequestBody NotificationRequestDto requestDto) {
        Timestamp sendTime = Timestamp.valueOf(requestDto.getSendTime().toLocalDateTime());
        notificationService.createNotifications(requestDto.getFileName(), requestDto.getEventName(), sendTime);
        return ResponseEntity.ok("Notification creation initiated!");
    }
}