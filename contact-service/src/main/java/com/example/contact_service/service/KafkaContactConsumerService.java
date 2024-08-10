package com.example.contact_service.service;

import com.example.contact_service.entity.Contact;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class KafkaContactConsumerService {
    private static final Logger logger = LoggerFactory.getLogger(KafkaContactConsumerService.class);

    @KafkaListener(topics = "files-topic", groupId = "contact-group")
    public void consume(Contact contact) {
        logger.info("Received contact from Kafka: {}", contact);
    }
}
