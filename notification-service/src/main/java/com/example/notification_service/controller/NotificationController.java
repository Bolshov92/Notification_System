package com.example.notification_service.controller;

import com.example.notification_service.service.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

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
    public String createNotifications(@RequestParam String fileName,
                                      @RequestParam String eventName,
                                      @RequestParam Timestamp sendTime) {
        notificationService.createNotifications(fileName, eventName, sendTime);
        return "Notification creation initiated!";
    }
}