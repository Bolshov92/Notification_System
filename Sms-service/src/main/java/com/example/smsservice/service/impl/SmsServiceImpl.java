package com.example.smsservice.service.impl;

import com.example.smsservice.entity.SmsLog;
import com.example.smsservice.service.SmsService;
import com.example.smsservice.smsRepository.SmsRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.net.URI;
import java.sql.Timestamp;
import java.util.HashMap;
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

    @Autowired
    private SmsRepository smsRepository;

    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    @PostConstruct
    private void init() {
        Twilio.init(accountSid, authToken);
    }

    @Override
    public void sendSms(Long notificationId, String to, String text) {
        String messageSid = null;
        try {
            Message message = Message.creator(
                            new PhoneNumber(to),
                            new PhoneNumber(fromPhoneNumber),
                            text
                    )
                    .setStatusCallback(URI.create("https://ba67-2a00-23c6-2936-f601-d0a6-2896-ecb3-4c32.ngrok-free.app/api/sms/status"))
                    .create();

            messageSid = message.getSid();
            logger.info("SMS sent successfully to {} with MessageSid {}", to, messageSid);
        } catch (Exception e) {
            logger.error("Failed to send SMS to {}: {}", to, e.getMessage());
        }

        saveSmsLog(notificationId, messageSid, to, text, "PENDING");
    }

    private void saveSmsLog(Long notificationId, String messageSid, String to, String text, String status) {
        SmsLog smsLog = new SmsLog();
        smsLog.setNotificationId(notificationId);
        smsLog.setMessageSid(messageSid);
        smsLog.setToPhoneNumber(to);
        smsLog.setMessage(text);
        smsLog.setStatus(status);
        smsLog.setSentAt(new Timestamp(System.currentTimeMillis()));
        smsRepository.save(smsLog);
    }

    public void sendStatusUpdate(String messageSid, String messageStatus) {
        try {
            Map<String, Object> statusUpdate = new HashMap<>();
            statusUpdate.put("messageSid", messageSid);
            statusUpdate.put("status", messageStatus);

            SmsLog smsLog = smsRepository.findByMessageSid(messageSid);
            if (smsLog != null) {
                smsLog.setStatus(messageStatus);
                smsRepository.save(smsLog);
                statusUpdate.put("notificationId", smsLog.getNotificationId());

                String statusUpdateJson = objectMapper.writeValueAsString(statusUpdate);
                kafkaTemplate.send("notification-status-topic", statusUpdateJson);

                logger.info("Sent status update to Kafka for MessageSid {}: {}", messageSid, statusUpdateJson);
            } else {
                logger.error("SmsLog not found for MessageSid {}", messageSid);
            }
        } catch (Exception e) {
            logger.error("Failed to send status update to Kafka: {}", e.getMessage());
        }
    }

    @KafkaListener(topics = "sms-notifications-topic", groupId = "sms-service-group")
    public void listen(String message) {
        logger.info("Received message from Kafka: {}", message);

        try {
            Map<String, Object> messageMap = objectMapper.readValue(message, Map.class);

            Long notificationId = getLongValue(messageMap, "notificationId");
            String contactName = getStringValue(messageMap, "contactName");
            String phoneNumber = getStringValue(messageMap, "phoneNumber");
            String event = getStringValue(messageMap, "event");
            String textMessage = getStringValue(messageMap, "message");

            if (notificationId == null) {
                logger.error("Notification ID is null. Cannot process message.");
                return;
            }

            String fullMessage = String.format("Hello %s,\n\nEvent: %s\nMessage: %s", contactName, event, textMessage);
            sendSms(notificationId, phoneNumber, fullMessage);
        } catch (Exception e) {
            logger.error("Failed to process message", e);
        }
    }

    private Long getLongValue(Map<String, Object> map, String key) {
        Object value = map.get(key);
        if (value instanceof Number) {
            return ((Number) value).longValue();
        }
        return null;
    }

    private String getStringValue(Map<String, Object> map, String key) {
        Object value = map.get(key);
        return value != null ? value.toString() : "";
    }
}
