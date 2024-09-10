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
    private KafkaTemplate<String, Object> kafkaTemplate;

    @Autowired
    private NotificationRepository notificationRepository;

    private static final String SMS_TOPIC = "sms-notifications-topic";
    private static final String NOTIFICATION_TOPIC = "notification-topic";

    private Map<String, String> contactCache = new HashMap<>();
    private Map<String, String> eventCache = new HashMap<>();

    @KafkaListener(topics = "contact-topic", groupId = "notification-group")
    public void handleContactEvent(String message) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        Map<String, Object> contactDetails = objectMapper.readValue(message, Map.class);
        Long contactId = Long.valueOf(String.valueOf(contactDetails.get("id")));
        String phoneNumber = (String) contactDetails.get("phoneNumber");
        String contactName = (String) contactDetails.get("name");

        contactCache.put(contactId.toString(), contactName + "," + phoneNumber);
    }

    @KafkaListener(topics = "event-topic", groupId = "notification-group")
    public void handleEventEvent(String message) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        Map<String, Object> eventDetails = objectMapper.readValue(message, Map.class);
        Long eventId = Long.valueOf(String.valueOf(eventDetails.get("id")));
        String eventMessage = (String) eventDetails.get("notificationText");

        eventCache.put(eventId.toString(), eventMessage);
    }

    @KafkaListener(topics = NOTIFICATION_TOPIC, groupId = "notification-group")
    public void handleNotificationEvent(String message) {
        System.out.println("Received message: " + message);
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            Map<String, Object> notificationDetails = objectMapper.readValue(message, Map.class);

            String contactIdStr = String.valueOf(notificationDetails.get("contact_id"));
            String eventIdStr = String.valueOf(notificationDetails.get("event_id")); // Обязательно добавьте event_id в контакт-сервис
            String contactName = String.valueOf(notificationDetails.get("contact_name"));
            String phoneNumber = String.valueOf(notificationDetails.get("phone_number"));
            String messageText = String.valueOf(notificationDetails.get("message")); // Если вы отправляете message из event-сервиса

            Long contactId = contactIdStr.isEmpty() ? null : Long.valueOf(contactIdStr);
            Long eventId = eventIdStr.isEmpty() ? null : Long.valueOf(eventIdStr);

            if (contactId != null && contactCache.containsKey(contactId.toString())) {
                String[] contactParts = contactCache.get(contactId.toString()).split(",");
                contactName = contactParts[0];
                phoneNumber = contactParts[1];
            }

            if (eventId != null && eventCache.containsKey(eventId.toString())) {
                messageText = eventCache.get(eventId.toString());
            }

            if (contactId == null || phoneNumber == null || messageText == null) {
                System.err.println("Missing data in notification: contact_id=" + contactId + ", phone_number=" + phoneNumber + ", message=" + messageText);
                return;
            }

            Notification notification = new Notification();
            notification.setEventId(eventId);
            notification.setContactId(contactId);
            notification.setContactName(contactName);
            notification.setPhoneNumber(phoneNumber);
            notification.setMessage(messageText);
            notification.setStatus("Sent");
            notification.setSentAt(new Timestamp(System.currentTimeMillis()));

            notificationRepository.save(notification);
            System.out.println("Notification saved to database: " + notification);

            kafkaTemplate.send(SMS_TOPIC, "To: " + phoneNumber + ", Message: " + messageText);
            System.out.println("SMS notification sent: " + messageText);
        } catch (Exception e) {
            System.err.println("Failed to process notification message: " + e.getMessage());
        }
    }

    @Override
    public void sendNotification(String to, String message) {
        Notification notification = new Notification();
        notification.setMessage(message);
        notification.setStatus("Sent");
        notification.setSentAt(new Timestamp(System.currentTimeMillis()));
        notification.setPhoneNumber(to);

        try {
            kafkaTemplate.send(NOTIFICATION_TOPIC, notification);
            notificationRepository.save(notification);
            System.out.println("Notification sent to Kafka and saved: " + message);
        } catch (Exception e) {
            System.err.println("Failed to send notification: " + e.getMessage());
        }
    }
}
