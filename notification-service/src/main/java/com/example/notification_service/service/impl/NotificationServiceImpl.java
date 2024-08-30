package com.example.notification_service.service.impl;

import com.example.notification_service.entity.Notification;
import com.example.notification_service.repository.NotificationRepository;
import com.example.notification_service.service.NotificationService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.HashMap;
import java.util.Map;

@Service
public class NotificationServiceImpl implements NotificationService {

    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    @Autowired
    private NotificationRepository notificationRepository;

    private static final String SMS_TOPIC = "sms-notifications-topic";
    private static final String NOTIFICATION_TOPIC = "notifications-topic";

    private Map<String, String> contactCache = new HashMap<>();
    private Map<String, String> eventCache = new HashMap<>();

    @KafkaListener(topics = "contact-topic", groupId = "notification-group")
    public void handleContactEvent(String message) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        Map<String, Object> contactDetails = objectMapper.readValue(message, Map.class);
        String contactId = String.valueOf(contactDetails.get("id"));
        String phoneNumber = (String) contactDetails.get("phoneNumber");

        contactCache.put(contactId, phoneNumber);
    }

    @KafkaListener(topics = "event-topic", groupId = "notification-group")
    public void handleEventEvent(String message) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        Map<String, Object> eventDetails = objectMapper.readValue(message, Map.class);
        String eventId = String.valueOf(eventDetails.get("id"));
        String eventMessage = (String) eventDetails.get("notificationText");

        eventCache.put(eventId, eventMessage);
    }

    @KafkaListener(topics = NOTIFICATION_TOPIC, groupId = "notification-group")
    public void handleNotificationEvent(String message) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        Map<String, Object> notificationDetails = objectMapper.readValue(message, Map.class);
        String contactId = String.valueOf(notificationDetails.get("contactId"));
        String eventId = String.valueOf(notificationDetails.get("eventId"));

        String phoneNumber = contactCache.get(contactId);
        String eventMessage = eventCache.get(eventId);

        if (phoneNumber != null && eventMessage != null) {
            Notification notification = new Notification();
            notification.setContactId(Long.valueOf(contactId));
            notification.setEventId(Long.valueOf(eventId));
            notification.setMessage(eventMessage);
            notification.setStatus("Sent");
            notification.setSentAt(new Timestamp(System.currentTimeMillis()));

            notificationRepository.save(notification);
            kafkaTemplate.send(SMS_TOPIC, notification.getMessage());

            System.out.println("SMS notification sent: " + notification.getMessage());
        } else {
            System.out.println("Failed to send SMS: Missing contact or event information.");
        }
    }

    @Override
    public void sendNotification(String to, String message) {
        kafkaTemplate.send(NOTIFICATION_TOPIC, message);

        Notification notification = new Notification();
        notification.setMessage(message);
        notification.setStatus("Sent");
        notification.setSentAt(new Timestamp(System.currentTimeMillis()));
        notificationRepository.save(notification);

        System.out.println("Notification sent to Kafka: " + message);
    }
}
