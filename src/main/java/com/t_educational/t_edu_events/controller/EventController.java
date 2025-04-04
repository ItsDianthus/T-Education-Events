package com.t_educational.t_edu_events.controller;

import com.t_educational.t_edu_events.model.Event;
import com.t_educational.t_edu_events.service.EventService;
import org.springframework.http.ResponseEntity;
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

    // POST api/admin/events - добавить мероприятие
    @PostMapping("/admin/events")
    public ResponseEntity<Event> addEvent(@RequestBody Event event) {
        Event createdEvent = eventService.addEvent(event);
        return ResponseEntity.ok(createdEvent);
    }

    // DELETE api/admin/events/{id} - удалить мероприятие по ID
    @DeleteMapping("/admin/events/{id}")
    public ResponseEntity<Void> deleteEvent(@PathVariable Long id) {
        boolean deleted = eventService.deleteEvent(id);
        if (deleted) {
            return ResponseEntity.noContent().build(); // 204 No Content
        } else {
            return ResponseEntity.notFound().build(); // 404 Not Found
        }
    }
}
