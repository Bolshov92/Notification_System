package com.example.event_service.service.impl;

import com.example.event_service.entity.Event;
import com.example.event_service.repository.EventRepository;
import com.example.event_service.service.EventService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class EventServiceImpl implements EventService {
    private static final String TOPIC = "notification-topic";

    @Autowired
    private EventRepository eventRepository;

    @Autowired
    private KafkaTemplate<String, Object> kafkaTemplate;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public Event createEvent(Event event) {
        Event savedEvent = eventRepository.save(event);
        sendEventToNotificationTopic(savedEvent);
        return savedEvent;
    }

    @Override
    public List<Event> getAllEvents() {
        return eventRepository.findAll();
    }

    @Override
    public Event getEventById(Long id) {
        return eventRepository.findById(id).orElse(null);
    }

    @Override
    public void deleteEvent(Long id) {
        eventRepository.deleteById(id);
    }

    private void sendEventToNotificationTopic(Event event) {
        try {

            Map<String, Object> eventDetails = new HashMap<>();
            eventDetails.put("event_id", event.getId());
            eventDetails.put("event_name", event.getEventName());
            eventDetails.put("event_date", event.getEventDate());
            eventDetails.put("notification_text", event.getNotificationText());

            String eventJson = objectMapper.writeValueAsString(eventDetails);

            kafkaTemplate.send(TOPIC, eventJson);
            System.out.println("Sent event as JSON to notification-topic: " + eventJson);
        } catch (JsonProcessingException e) {
            System.err.println("Failed to serialize event: " + e.getMessage());
        }
    }
}
