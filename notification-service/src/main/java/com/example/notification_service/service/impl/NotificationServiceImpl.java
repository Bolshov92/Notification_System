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
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class NotificationServiceImpl implements NotificationService {
    private static final Logger logger = LoggerFactory.getLogger(NotificationServiceImpl.class);

    private final KafkaTemplate<String, String> kafkaTemplate;
    private final NotificationRepository notificationRepository;
    private final ThreadPoolTaskScheduler taskScheduler;

    private List<String> contactsList = new ArrayList<>();
    private String eventDetails;
    private String requestedFileName;
    private String requestedEventName;
    private LocalDateTime notificationTime;

    private final ObjectMapper objectMapper = new ObjectMapper();

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
        this.notificationTime = sendTime.toLocalDateTime();

        requestContacts(fileName);
        requestEvent(eventName);

        new Thread(() -> {
            try {
                while (eventDetails == null || contactsList.isEmpty()) {
                    Thread.sleep(1000);
                }
                saveNotifications(fileName, eventName, sendTime.toLocalDateTime(), contactsList, eventDetails);
                scheduleNotificationSending();
            } catch (InterruptedException e) {
                logger.error("Error waiting for data: ", e);
                Thread.currentThread().interrupt();
            }
        }).start();
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
    }

    @KafkaListener(topics = "event-response-topic", groupId = "notification_group")
    public void processEventResponse(String message) {
        logger.info("Received event response ->: {}", message);
        eventDetails = message;
    }

    private void saveNotifications(String fileName, String eventNameParam, LocalDateTime sendTime,
                                   List<String> contactsList, String eventDetails) {
        if (eventDetails == null) {
            logger.error("Event details are null. Can't save notifications.");
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
            logger.error("Event details are incomplete. Can't save notifications.");
            return;
        }

        ZonedDateTime zonedSendTime = sendTime.atZone(ZoneId.of("Europe/London"));

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


            Notification existingNotification = notificationRepository
                    .findByEventNameAndPhoneNumberAndContactIdAndNotificationTime(eventName, phoneNumber, contactId, zonedSendTime.toLocalDateTime());

            if (existingNotification != null) {
                logger.info("Notification for event '{}' and phone number '{}' already exists with the same send time. Skipping.", eventName, phoneNumber);
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
            notification.setNotificationTime(zonedSendTime.toLocalDateTime());

            notificationRepository.save(notification);
        }

        clearTemporaryData();
    }





    private void scheduleNotificationSending() {
        if (notificationTime == null) {
            logger.error("Notification time is null. Cannot schedule sending.");
            return;
        }

        ZonedDateTime londonTime = notificationTime.atZone(ZoneId.of("Europe/London"));

        long delay = londonTime.toInstant().toEpochMilli() - System.currentTimeMillis();

        logger.info("Scheduling notification sending. Delay: {} ms", delay);

        if (delay > 0) {
            taskScheduler.schedule(this::sendNotifications, new Date(System.currentTimeMillis() + delay));
        } else {
            logger.warn("Scheduled time is in the past. Sending notifications immediately.");
            sendNotifications();
        }
    }


    private void sendNotifications() {
        List<Notification> notifications = notificationRepository.findByNotificationTimeAfterAndStatus(LocalDateTime.now(), "PENDING");

        for (Notification notification : notifications) {
            notifySmsService(notification.getId(), notification.getContactName(), notification.getPhoneNumber(),
                    notification.getEventName(), notification.getEventMessage());
            notification.setStatus("SENT");
            notificationRepository.save(notification);
        }

        logger.info("Notifications sent and statuses updated.");
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

    private void clearTemporaryData() {
        contactsList.clear();
        eventDetails = null;
        requestedFileName = null;
        requestedEventName = null;
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

    @KafkaListener(topics = "notification-status-topic", groupId = "notification-service-group")
    public void processStatusUpdate(String message) {
        logger.info("Received status update: {}", message);
        try {
            Map<String, Object> statusUpdate = objectMapper.readValue(message, Map.class);
            Long notificationId = getLongValue(statusUpdate, "notificationId");
            String status = getStringValue(statusUpdate, "status");

            if (notificationId == null) {
                logger.error("Notification ID is null. Cannot process status update.");
                return;
            }

            Notification notification = notificationRepository.findById(notificationId).orElse(null);
            if (notification != null) {
                notification.setStatus(status);
                notificationRepository.save(notification);
                logger.info("Updated notification status for notificationId {}: {}", notificationId, status);
            } else {
                logger.warn("Notification not found for notificationId {}", notificationId);
            }
        } catch (Exception e) {
            logger.error("Failed to process status update", e);
        }
    }
}
