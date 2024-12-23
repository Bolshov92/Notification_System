package com.example.event_service.service.impl;

import com.example.event_service.dto.EventDTO;
import com.example.event_service.entity.Event;
import com.example.event_service.repository.EventRepository;
import com.example.event_service.service.EventService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class EventServiceImpl implements EventService {

    private static final Logger logger = LoggerFactory.getLogger(EventServiceImpl.class);
    private static final String EVENT_REQUEST_TOPIC = "event-request-topic";
    private static final String EVENT_RESPONSE_TOPIC = "event-response-topic";

    @Autowired
    private EventRepository eventRepository;

    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public ResponseEntity<String> createEvent(EventDTO eventDTO) {
        Optional<Event> existingEvent = eventRepository.findByEventName(eventDTO.getEventName());

        if (existingEvent.isPresent()) {
            return ResponseEntity.badRequest().body("Event with the name '" + eventDTO.getEventName() + "' already exists.");
        }


        Event event = new Event();
        event.setEventName(eventDTO.getEventName());
        event.setEventMessage(eventDTO.getEventMessage());
        eventRepository.save(event);

        return ResponseEntity.ok("Event with the name '" + eventDTO.getEventName() + "' has been successfully created.");
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

    @KafkaListener(topics = EVENT_REQUEST_TOPIC, groupId = "event-group")
    public void processEventRequest(String message) {
        try {
            JsonNode requestJson = objectMapper.readTree(message);
            String eventName = requestJson.get("eventName").asText();

            Optional<Event> events = eventRepository.findByEventName(eventName);
            if (!events.isEmpty()) {
                Event event = events.get();

                Map<String, Object> response = new HashMap<>();
                response.put("eventId", event.getId());
                response.put("eventName", event.getEventName());
                response.put("eventMessage", event.getEventMessage());

                String responseJson = objectMapper.writeValueAsString(response);
                kafkaTemplate.send(EVENT_RESPONSE_TOPIC, responseJson);
            } else {
                logger.warn("Event not found: {}", eventName);
            }
        } catch (JsonProcessingException e) {
            logger.error("Error processing event request: ", e);
        }
    }
}
