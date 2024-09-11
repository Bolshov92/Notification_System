package com.example.file_service.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class FileProducer {

    private static final String TOPIC = "contacts-topic";

    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;
    @Autowired
    private ObjectMapper objectMapper;

    public void sendMessage( Long id ,String name, String phoneNumber) {
        try {
            String jsonMessage = objectMapper.writeValueAsString(Map.of(
                    "file_id", id,
                    "name", name,
                    "phoneNumber", phoneNumber
            ));
            kafkaTemplate.send(TOPIC, jsonMessage);
            System.out.println("Message sent to Kafka: " + jsonMessage);
        } catch (Exception e) {
            System.err.println("Failed to send message to Kafka: " + e.getMessage());
        }
    }
}