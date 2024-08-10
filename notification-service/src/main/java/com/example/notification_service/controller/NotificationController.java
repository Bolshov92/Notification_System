package com.example.notification_service.controller;

import com.example.notification_service.service.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/notifications")
public class NotificationController {

    @Autowired
    private NotificationService notificationService;

    @PostMapping("/send-sms")
    public ResponseEntity<String> sendSms(@RequestParam String to, @RequestParam String message) {
        try {
            notificationService.sendNotification(to, message);
            return ResponseEntity.ok("SMS notification sent successfully.");
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Failed to send SMS notification: " + e.getMessage());
        }
    }
}