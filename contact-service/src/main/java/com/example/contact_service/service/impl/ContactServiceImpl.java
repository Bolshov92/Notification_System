package com.example.contact_service.service.impl;

import com.example.contact_service.entity.Contact;
import com.example.contact_service.service.ContactService;
import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvValidationException;
import lombok.Data;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

@Data
@Service
public class ContactServiceImpl implements ContactService {
    private static final Logger logger = LoggerFactory.getLogger(ContactServiceImpl.class);

    @Autowired
    private KafkaTemplate<String, Object> kafkaTemplate;

    @KafkaListener(topics = "contacts-topic", groupId = "contact-service-group")
    public void listen(String message) {
        logger.info("Received contact from Kafka: {}", message);

        String[] parts = message.split(",");
        if (parts.length >= 2) {
            String name = parts[0].trim();
            String phoneNumber = parts[1].trim();
            Contact contact = new Contact(name, phoneNumber);
            kafkaTemplate.send("sms-topic", contact);
        } else {
            logger.error("Invalid contact format received: {}", message);
        }
    }

}