package com.emergency.service;

import com.emergency.entity.Event;

import java.util.List;

public interface EventService {
    Event createEvent(Event event);

    List<Event> getAllEvents();

    Event getEventById(Long id);

    void deleteEvent(Long id);

}
