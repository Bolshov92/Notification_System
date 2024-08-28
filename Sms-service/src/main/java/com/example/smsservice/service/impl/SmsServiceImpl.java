package com.example.smsservice.service.impl;

import com.example.smsservice.service.SmsService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.Map;

@Service
public class SmsServiceImpl implements SmsService {

    private static final Logger logger = LoggerFactory.getLogger(SmsServiceImpl.class);

    @Value("${twilio.account-sid}")
    private String accountSid;

    @Value("${twilio.auth-token}")
    private String authToken;

    @Value("${twilio.phone-number}")
    private String fromPhoneNumber;

    @Autowired
    private ObjectMapper objectMapper;

    @PostConstruct
    private void init() {
        Twilio.init(accountSid, authToken);
    }

    @Override
    public void sendSms(String to, String text) {
        try {
            Message message = Message.creator(
                    new PhoneNumber(to),
                    new PhoneNumber(fromPhoneNumber),
                    text
            ).create();

            logger.info("SMS sent successfully to {}", to);
        } catch (Exception e) {
            logger.error("Failed to send SMS to {}: {}", to, e.getMessage());
            throw new RuntimeException("Failed to send SMS", e);
        }
    }

    @KafkaListener(topics = "contacts-topic", groupId = "sms-service-group")
    public void listen(String message) {
        logger.info("Received message from Kafka: {}", message);

        try {
            // Преобразуем JSON-строку в Map
            Map<String, String> contactMap = objectMapper.readValue(message, Map.class);

            String name = contactMap.get("name");
            String phoneNumber = contactMap.get("phoneNumber");

            sendSms(phoneNumber, "Welcome " + name + "!");
        } catch (Exception e) {
            logger.error("Failed to process message", e);
        }
    }
}