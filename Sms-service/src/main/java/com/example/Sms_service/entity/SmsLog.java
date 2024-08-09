package com.example.Sms_service.entity;

import lombok.Data;

import javax.persistence.*;
import java.sql.Timestamp;

@Entity
@Data
@Table(name = "sms_ogs")
public class SmsLog {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "to_phone_number", nullable = false, length = 15)
    private String toPhoneNumber;

    @Column(name = "message", nullable = false, columnDefinition = "TEXT")
    private String message;

    @Column(name = "status", nullable = true, length = 20)
    private String status;

    @Column(name = "sent_at", nullable = false)
    private Timestamp sentAt;
}
