package com.t_educational.t_edu_events.controller.admin;

import com.t_educational.t_edu_events.model.Event;
import com.t_educational.t_edu_events.repository.EventRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/admin/events")
@RequiredArgsConstructor
public class AdminEventController {

    private final EventRepository eventRepository;

    // POST api/admin/events — создание нового мероприятия. При создании isActive всегда false.
    @PostMapping
    public ResponseEntity<Event> createEvent(@RequestBody Event event) {
        // По умолчанию ивенты неактивны
        event.setActive(false);
        Event savedEvent = eventRepository.save(event);
        return ResponseEntity.ok(savedEvent);
    }

    // GET /admin/events — получение списка всех мероприятий.
    @GetMapping
    public ResponseEntity<List<Event>> getAllEvents() {
        List<Event> events = eventRepository.findAll();
        return ResponseEntity.ok(events);
    }

    // GET /admin/events/{eventID} — получение детальной информации о мероприятии.
    @GetMapping("/{eventID}")
    public ResponseEntity<Event> getEvent(@PathVariable("eventID") UUID eventID) {
        return eventRepository.findById(eventID)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PutMapping("/{eventID}")
    public ResponseEntity<Event> updateEvent(@PathVariable("eventID") UUID eventID,
                                             @RequestBody Event updatedEvent) {
        return eventRepository.findById(eventID)
                .map(existingEvent -> {
                    if (updatedEvent.isActive() != existingEvent.isActive()) {
                        throw new IllegalArgumentException("Поле active редактируется отдельно.");
                    }
                    existingEvent.setName(updatedEvent.getName());
                    existingEvent.setStartDate(updatedEvent.getStartDate());
                    existingEvent.setEndDate(updatedEvent.getEndDate());
                    // Поле active не меняем
                    Event saved = eventRepository.save(existingEvent);
                    return ResponseEntity.ok(saved);
                })
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    // DELETE /admin/events/{eventID} — удаление мероприятия.
    @DeleteMapping("/{eventID}")
    public ResponseEntity<Object> deleteEvent(@PathVariable("eventID") UUID eventID) {
        return eventRepository.findById(eventID)
                .map(event -> {
                    eventRepository.delete(event);
                    return ResponseEntity.noContent().build();
                })
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    // PUT /admin/event/{id}/activate — активация мероприятия (isActive = true)
    @PutMapping("/{eventID}/activate")
    public ResponseEntity<Event> activateEvent(@PathVariable("eventID") UUID eventID) {
        return eventRepository.findById(eventID)
                .map(event -> {
                    event.setActive(true);
                    Event updated = eventRepository.save(event);
                    return ResponseEntity.ok(updated);
                })
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    // PUT /admin/event/{id}/deactivate — деактивация мероприятия (isActive = false)
    @PutMapping("/{eventID}/deactivate")
    public ResponseEntity<Event> deactivateEvent(@PathVariable("eventID") UUID eventID) {
        return eventRepository.findById(eventID)
                .map(event -> {
                    event.setActive(false);
                    Event updated = eventRepository.save(event);
                    return ResponseEntity.ok(updated);
                })
                .orElseGet(() -> ResponseEntity.notFound().build());
    }
}
