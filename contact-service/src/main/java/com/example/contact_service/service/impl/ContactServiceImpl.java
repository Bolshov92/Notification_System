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
import java.util.List;
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

    @KafkaListener(topics = "contacts-topic", groupId = "contact-group")
    public void consume(String message) {
        try {
            Map<String, Object> contactData = objectMapper.readValue(message, Map.class);

            Long fileId = Long.valueOf(contactData.get("file_id").toString());
            String fileName = contactData.get("file_name").toString();
            String name = contactData.get("name").toString();
            String phoneNumber = contactData.get("phoneNumber").toString();

            Contact contact = new Contact();
            contact.setFileName(fileName);
            contact.setFileId(fileId);
            contact.setName(name);
            contact.setPhoneNumber(phoneNumber);

            contactRepository.save(contact);
            logger.info("Saved contact to database: {}", contact);

        } catch (JsonProcessingException e) {
            logger.error("Failed to parse JSON message: " + e.getMessage(), e);
        } catch (Exception e) {
            logger.error("Failed to process message: " + e.getMessage(), e);
        }
    }

    @KafkaListener(topics = "contact-request-topic", groupId = "contact-group")
    public void consumeContactRequest(String message) {
        try {
            Map<String, String> request = objectMapper.readValue(message, Map.class);
            String fileName = request.get("fileName");

            List<Contact> contacts = contactRepository.findByFileName(fileName);

            for (Contact contact : contacts) {
                String contactJson = objectMapper.writeValueAsString(contact);
                kafkaTemplate.send("contact-response-topic", contactJson);
            }
        } catch (JsonProcessingException e) {
            logger.error("Failed to parse JSON message: " + e.getMessage(), e);
        } catch (Exception e) {
            logger.error("Failed to process message: " + e.getMessage(), e);
        }
    }
}
