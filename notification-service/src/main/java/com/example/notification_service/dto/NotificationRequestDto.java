package com.example.notification_service.dto;

import lombok.Data;

import java.sql.Timestamp;

@Data
public class NotificationRequestDto {
    private String fileName;
    private String eventName;
    private Timestamp sendTime;
}
