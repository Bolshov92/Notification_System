package com.example.contact_service.service;

import com.example.contact_service.entity.Contact;
import com.example.contact_service.service.impl.ContactServiceImpl;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class KafkaContactConsumerService {
    private static final Logger logger = LoggerFactory.getLogger(KafkaContactConsumerService.class);
    @Autowired
    private ContactServiceImpl contactService;

    @Autowired
    private ObjectMapper objectMapper;

    @KafkaListener(topics = "contacts-topic", groupId = "contact-group")
    public void consume(String message) {
        try {
            Contact contact = objectMapper.readValue(message, Contact.class);
            contactService.sendContact(contact);
        } catch (Exception e) {
            logger.error("Failed to process message: " + e.getMessage());
        }
    }
}
