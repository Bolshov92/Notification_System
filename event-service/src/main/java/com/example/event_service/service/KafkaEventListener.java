package com.example.event_service.service;

import com.example.event_service.service.impl.EventServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class KafkaEventListener {
    @Autowired
    private EventServiceImpl   eventService;

    @KafkaListener(topics = "contact-topic", groupId = "event-service-group")
    public void listen(String contactJson){
        System.out.println("received new message from Kafka:" + contactJson);
        eventService.createEventFromKafka(contactJson);
    }
}
