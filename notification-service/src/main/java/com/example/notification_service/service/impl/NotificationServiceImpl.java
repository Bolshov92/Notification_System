package com.example.notification_service.service.impl;

import com.example.notification_service.entity.Notification;
import com.example.notification_service.repository.NotificationRepository;
import com.example.notification_service.service.NotificationService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

@Service
public class NotificationServiceImpl implements NotificationService {
    private static final Logger logger = LoggerFactory.getLogger(NotificationServiceImpl.class);

    private final KafkaTemplate<String, String> kafkaTemplate;
    private final NotificationRepository notificationRepository;
    private final ThreadPoolTaskScheduler taskScheduler;

    private final ObjectMapper objectMapper = new ObjectMapper();

    private List<String> contactsList = new ArrayList<>();
    private String eventDetails;
    private String requestedFileName;
    private String requestedEventName;
    private Timestamp notificationTime;

    private boolean notificationsSaved = false;

    @Autowired
    public NotificationServiceImpl(KafkaTemplate<String, String> kafkaTemplate,
                                   NotificationRepository notificationRepository,
                                   ThreadPoolTaskScheduler taskScheduler) {
        this.kafkaTemplate = kafkaTemplate;
        this.notificationRepository = notificationRepository;
        this.taskScheduler = taskScheduler;
    }

    @Override
    public void createNotifications(String fileName, String eventName, Timestamp sendTime) {
        this.requestedFileName = fileName;
        this.requestedEventName = eventName;
        this.notificationTime = sendTime;

        requestContacts(fileName);
        requestEvent(eventName);
    }

    private void requestContacts(String fileName) {
        String requestJson = "{\"fileName\":\"" + fileName + "\"}";
        kafkaTemplate.send("contact-request-topic", requestJson);
    }

    private void requestEvent(String eventName) {
        String requestJson = "{\"eventName\":\"" + eventName + "\"}";
        kafkaTemplate.send("event-request-topic", requestJson);
    }

    @KafkaListener(topics = "contact-response-topic", groupId = "notification_group")
    public void processContactResponse(String message) {
        logger.info("Received contact response: {}", message);
        contactsList.add(message);
        checkAndSaveNotifications();
    }

    @KafkaListener(topics = "event-response-topic", groupId = "notification_group")
    public void processEventResponse(String message) {
        logger.info("Received event response: {}", message);
        eventDetails = message;
        checkAndSaveNotifications();
    }

    private synchronized void checkAndSaveNotifications() {
        if (eventDetails != null && !contactsList.isEmpty() && !notificationsSaved) {
            saveNotifications(requestedFileName, requestedEventName, notificationTime, contactsList, eventDetails);
            notificationsSaved = true;
            scheduleNotificationSending();
        }
    }

    private void saveNotifications(String fileName, String eventNameParam, Timestamp sendTime,
                                   List<String> contactsList, String eventDetails) {
        if (eventDetails == null) {
            logger.error("Event details are null. Cannot save notifications.");
            return;
        }

        Map<String, Object> eventMap;
        try {
            eventMap = objectMapper.readValue(eventDetails, Map.class);
        } catch (Exception e) {
            logger.error("Error parsing event details: ", e);
            return;
        }

        Long eventId = getLongValue(eventMap, "eventId");
        String eventName = getStringValue(eventMap, "eventName");
        String eventMessage = getStringValue(eventMap, "eventMessage");

        if (eventId == null || eventName.isEmpty() || eventMessage.isEmpty()) {
            logger.error("Event details are incomplete. Cannot save notifications.");
            return;
        }

        for (String contactJson : contactsList) {
            Map<String, Object> contactMap;
            try {
                contactMap = objectMapper.readValue(contactJson, Map.class);
            } catch (Exception e) {
                logger.error("Error parsing contact details: ", e);
                continue;
            }

            Long contactId = getLongValue(contactMap, "id");
            String contactName = getStringValue(contactMap, "name");
            String phoneNumber = getStringValue(contactMap, "phoneNumber");

            if (contactId == null || contactName.isEmpty() || phoneNumber.isEmpty()) {
                logger.warn("Contact details are incomplete. Skipping contact.");
                continue;
            }

            Notification notification = new Notification();
            notification.setEventId(eventId);
            notification.setEventName(eventName);
            notification.setEventMessage(eventMessage);
            notification.setContactId(contactId);
            notification.setContactName(contactName);
            notification.setPhoneNumber(phoneNumber);
            notification.setStatus("PENDING");
            notification.setNotificationTime(sendTime.toLocalDateTime());

            notificationRepository.save(notification);
        }
    }

    private void scheduleNotificationSending() {
        long delay = notificationTime.getTime() - System.currentTimeMillis();
        logger.info("Scheduling notification sending. Delay: {} ms", delay);

        if (delay > 0) {
            taskScheduler.schedule(this::sendNotifications, new Date(System.currentTimeMillis() + delay));
        } else {
            logger.warn("Scheduled time is in the past. Sending notifications immediately.");
            sendNotifications();
        }
    }

    private void sendNotifications() {
        LocalDateTime now = LocalDateTime.now();
        List<Notification> notifications = notificationRepository.findByNotificationTimeBeforeAndStatus(now, "PENDING");

        for (Notification notification : notifications) {
            notifySmsService(notification.getId(), notification.getContactName(), notification.getPhoneNumber(),
                    notification.getEventName(), notification.getEventMessage());
            notification.setStatus("SENT");
            notificationRepository.save(notification);
        }

        logger.info("Notifications sent and statuses updated.");
    }

    private void notifySmsService(Long notificationId, String contactName, String phoneNumber, String eventName, String eventMessage) {
        Map<String, Object> smsNotification = new HashMap<>();
        smsNotification.put("notificationId", notificationId);
        smsNotification.put("contactName", contactName);
        smsNotification.put("phoneNumber", phoneNumber);
        smsNotification.put("event", eventName);
        smsNotification.put("message", eventMessage);

        try {
            String smsMessage = objectMapper.writeValueAsString(smsNotification);
            kafkaTemplate.send("sms-notifications-topic", smsMessage);
        } catch (Exception e) {
            logger.error("Failed to send SMS notification to Kafka: ", e);
        }
    }

    private Long getLongValue(Map<String, Object> map, String key) {
        Object value = map.get(key);
        if (value instanceof Number) {
            return ((Number) value).longValue();
        }
        return null;
    }

    private String getStringValue(Map<String, Object> map, String key) {
        Object value = map.get(key);
        return value != null ? value.toString() : "";
    }
}
