package com.example.event_service.service;

import com.example.event_service.dto.EventDTO;
import com.example.event_service.entity.Event;

import java.util.List;

public interface EventService {
    Event createEvent(EventDTO eventDTO);

    List<Event> getAllEvents();

    Event getEventById(Long id);

    void deleteEvent(Long id);

}
