package com.example.contact_service.service.impl;

import com.example.contact_service.entity.Contact;
import com.example.contact_service.repository.ContactRepository;
import com.example.contact_service.service.ContactService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class ContactServiceImpl implements ContactService {
    private static final Logger logger = LoggerFactory.getLogger(ContactServiceImpl.class);
    private static final String TOPIC = "notification-topic";

    @Autowired
    private KafkaTemplate<String, Object> kafkaTemplate;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private ContactRepository contactRepository;

    public void sendContact(Contact contact) {
        try {
            String contactJson = objectMapper.writeValueAsString(contact);
            kafkaTemplate.send(TOPIC, contactJson);
            logger.info("Sent contact as JSON to notification-topic:  {}", contactJson);
        } catch (JsonProcessingException e) {
            logger.error("Failed to convert contact to JSON", e);
        }
    }

    @KafkaListener(topics = "contacts-topic", groupId = "contact-group")
    public void consume(String message) {
        try {
            Contact contact = objectMapper.readValue(message, Contact.class);
            contactRepository.save(contact);
            logger.info("Saved contact to database: {}", contact);
            sendContact(contact);
        } catch (Exception e) {
            logger.error("Failed to process message: " + e.getMessage(), e);
        }
    }

}
