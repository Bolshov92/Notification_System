package com.example.event_service.service.impl;


import com.example.event_service.entity.Event;
import com.example.event_service.repository.EventRepository;
import com.example.event_service.service.EventService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class EventServiceImpl implements EventService {
    private static final String TOPIC = "events-topic";

    @Autowired
    private EventRepository eventRepository;

    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    @Override
    public Event createEvent(Event event) {
        Event savedEvent = eventRepository.save(event);
        String message = "Event: " + savedEvent.getEventName() + "| Date: " + savedEvent.getEventDate() + "| Text:  "  + savedEvent.getNotificationText();
        kafkaTemplate.send(TOPIC, message);
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

    private void sendEventToKafka(Event event) {
        String eventJson = String.format(
                "{\"id\":%d,\"eventName\":\"%s\",\"eventDate\":\"%s\",\"notificationText\":\"%s\"}",
                event.getId(), event.getEventName(), event.getEventDate(), event.getNotificationText()
        );
        kafkaTemplate.send(TOPIC, eventJson);
    }
}
