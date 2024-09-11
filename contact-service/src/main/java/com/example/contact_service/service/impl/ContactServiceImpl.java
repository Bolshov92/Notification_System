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

import java.util.HashMap;
import java.util.Map;

@Service
public class ContactServiceImpl implements ContactService {
    private static final Logger logger = LoggerFactory.getLogger(ContactServiceImpl.class);
    private static final String TOPIC = "notification-topic";

    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private ContactRepository contactRepository;

    public void sendContact(Contact contact) {
        try {
            Map<String, Object> contactDetails = new HashMap<>();
            contactDetails.put("contact_id", contact.getId());
            contactDetails.put("contact_name", contact.getName());
            contactDetails.put("phone_number", contact.getPhoneNumber());

            String contactJson = objectMapper.writeValueAsString(contactDetails);
            kafkaTemplate.send(TOPIC, contactJson);
            logger.info("Sent contact as JSON to notification-topic: {}", contactJson);
        } catch (JsonProcessingException e) {
            logger.error("Failed to convert contact to JSON", e);
        }
    }

    @KafkaListener(topics = "contacts-topic", groupId = "contact-group")
    public void consume(String message) {
        try {
            Map<String, Object> contactData = objectMapper.readValue(message, Map.class);

            Long fileId = Long.valueOf(contactData.get("fileId").toString());
            String name = contactData.get("name").toString();
            String phoneNumber = contactData.get("phoneNumber").toString();

            Contact contact = new Contact();
            contact.setFileId(fileId);
            contact.setName(name);
            contact.setPhoneNumber(phoneNumber);

            contactRepository.save(contact);
            logger.info("Saved contact to database: {}", contact);

            sendContact(contact);
        } catch (JsonProcessingException e) {
            logger.error("Failed to parse JSON message: " + e.getMessage(), e);
        } catch (Exception e) {
            logger.error("Failed to process message: " + e.getMessage(), e);
        }
    }
}
