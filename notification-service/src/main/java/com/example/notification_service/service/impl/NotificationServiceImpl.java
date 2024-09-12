package com.example.notification_service.service.impl;

import com.example.notification_service.repository.NotificationRepository;
import com.example.notification_service.service.NotificationService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

@Service
public class NotificationServiceImpl implements NotificationService {

    private static final Logger logger = LoggerFactory.getLogger(NotificationServiceImpl.class);
    private static final ObjectMapper objectMapper = new ObjectMapper();

    private final KafkaTemplate<String, String> kafkaTemplate;
    private final NotificationRepository notificationRepository;

    @Autowired
    public NotificationServiceImpl(KafkaTemplate<String, String> kafkaTemplate,
                                   NotificationRepository notificationRepository) {
        this.kafkaTemplate = kafkaTemplate;
        this.notificationRepository = notificationRepository;
    }
    @Override
    public void createNotifications(String fileName, String eventName, Timestamp sendTime) {
        requestContacts(fileName);
        requestEvent(eventName);

        List<JsonNode> contactsList = new ArrayList<>();
        JsonNode eventDetails = null;

        processResponses(fileName, eventName, sendTime, contactsList, eventDetails);
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
        try {
            JsonNode contactResponse = objectMapper.readTree(message);
        } catch (JsonProcessingException e) {
            logger.error("Ошибка обработки ответа от контактов: ", e);
        }
    }

    @KafkaListener(topics = "event-response-topic", groupId = "notification_group")
    public void processEventResponse(String message) {
        try {
            JsonNode eventResponse = objectMapper.readTree(message);
        } catch (JsonProcessingException e) {
            logger.error("Ошибка обработки ответа от событий: ", e);
        }
    }

    private void processResponses(String fileName, String eventName, Timestamp sendTime,
                                  List<JsonNode> contactsList, JsonNode eventDetails) {
    }
}
