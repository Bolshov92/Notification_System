package com.example.contact_service.service;

import com.example.contact_service.entity.Contact;
import com.example.contact_service.service.impl.ContactServiceImpl;
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

    @KafkaListener(topics = "files-topic", groupId = "contact-group")
    public void consume(String message) {
        try {
            String[] parts = message.split(",");
            if (parts.length >= 2) {
                String name = parts[0].trim();
                String phoneNumber = parts[1].trim();
                Contact contact = new Contact(name, phoneNumber);

                contactService.sendContact(contact);
            }
        } catch (Exception e) {

        }
    }
}
