package com.t_educational.t_edu_events.controller;

import com.t_educational.t_edu_events.model.Event;
import com.t_educational.t_edu_events.service.EventService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class EventController {

    private final EventService eventService;

    public EventController(EventService eventService) {
        this.eventService = eventService;
    }

    // GET api/events - получить все мероприятия
    @GetMapping("/events")
    @PreAuthorize("isAuthenticated()") // Только аутентифицированные пользователи
    public List<Event> getAllEvents() {
        return eventService.getAllEvents();
    }

}
