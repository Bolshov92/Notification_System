package com.example.notification_service.controller;

import com.example.notification_service.service.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/notifications")
public class NotificationController {
    @Autowired
    private NotificationService notificationService;

    @PostMapping("/send-sms")
    public Mono<Void> sendSms(@RequestParam String to, @RequestParam String message, ServerHttpRequest request) {
        request.getQueryParams().forEach((paramName, paramValues) -> {
            System.out.println("Parameter Name - " + paramName + ", Value - " + paramValues);
        });

        System.out.println("Received parameters - to: " + to + ", message: " + message);
        notificationService.sendNotification(to, message);
        return Mono.empty();
    }
}