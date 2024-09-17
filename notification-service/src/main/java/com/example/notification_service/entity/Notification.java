package com.example.notification_service.entity;

import lombok.Data;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Data
@Table(name = "notifications")
public class Notification {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "event_id")
    private Long eventId;

    @Column(name = "contact_id")
    private Long contactId;

    @Column(name = "contact_name")
    private String contactName;

    @Column(name = "phone_number")
    private String phoneNumber;

    @Column(name = "event_name")
    private String eventName;

    @Column(name = "event_message")
    private String eventMessage;

    @Column(name = "status", length = 50)
    private String status;

    @Column(name = "notification_time")
    private LocalDateTime notificationTime;
}
