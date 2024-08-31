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
        System.out.println("Received message: " + message);
        ObjectMapper objectMapper = new ObjectMapper();
        Map<String, Object> eventDetails = objectMapper.readValue(message, Map.class);

        Long contactId = Long.valueOf((String) eventDetails.get("contactId"));
        Long eventId = Long.valueOf((String) eventDetails.get("eventId"));
        String contactName = (String) eventDetails.get("contactName");
        String phoneNumber = (String) eventDetails.get("phoneNumber");
        String eventMessage = (String) eventDetails.get("message");

        Notification notification = new Notification();
        System.out.println("Deserialized Notification: " + notification);

        if (notification.getContactId() == null || notification.getEventId() == null ||
                notification.getPhoneNumber() == null || notification.getMessage() == null) {
            System.err.println("Invalid notification data: " + notification);
            return;
        }
        notification.setContactId(contactId);
        notification.setEventId(eventId);
        notification.setContactName(contactName);
        notification.setPhoneNumber(phoneNumber);
        notification.setMessage(eventMessage);
        notification.setStatus("Sent");
        notification.setSentAt(new Timestamp(System.currentTimeMillis()));

        notificationRepository.save(notification);
        kafkaTemplate.send(SMS_TOPIC, "To: " + phoneNumber + ", Message: " + eventMessage);

        System.out.println("SMS notification sent: " + notification.getMessage());
    }

    @Override
    public void sendNotification(String to, String message) {
        kafkaTemplate.send(NOTIFICATION_TOPIC, message);
        ObjectMapper objectMapper = new ObjectMapper();

        Notification notification = new Notification();
        notification.setMessage(message);
        notification.setStatus("Sent");
        notification.setSentAt(new Timestamp(System.currentTimeMillis()));
        try {
            String jsonMessage = objectMapper.writeValueAsString(notification);
            kafkaTemplate.send(NOTIFICATION_TOPIC, jsonMessage);
        } catch (JsonProcessingException e) {
            System.err.println("Failed to serialize notification: " + e.getMessage());
        }
        notificationRepository.save(notification);

        System.out.println("Notification sent to Kafka: " + message);
    }
}
