package com.example.file_service.service;

import com.example.file_service.entity.File;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class FileProducer {

    private static final String TOPIC = "contacts-topic";

    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    @Autowired
    private ObjectMapper objectMapper;

    public void sendMessage(File file) {
        try {
            String message = objectMapper.writeValueAsString(file);
            kafkaTemplate.send(TOPIC, message);
            System.out.println("Message sent to Kafka: " + message);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}