package com.t_educational.t_edu_events.controller.admin;

import com.t_educational.t_edu_events.dto.CategoryAssignmentDTO;
import com.t_educational.t_edu_events.model.Category;
import com.t_educational.t_edu_events.model.Event;
import com.t_educational.t_edu_events.repository.CategoryRepository;
import com.t_educational.t_edu_events.repository.EventRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Set;
import java.util.UUID;

@RestController
@RequestMapping("/api/admin/events/{eventID}/categories")
@RequiredArgsConstructor
public class EventCategoryAdminController {

    private final EventRepository eventRepository;
    private final CategoryRepository categoryRepository;

    // GET /admin/events/{eventID}/categories — получение списка направлений мероприятия.
    @GetMapping
    public ResponseEntity<Set<Category>> getEventCategories(@PathVariable("eventID") UUID eventID) {
        return eventRepository.findById(eventID)
                .map(event -> ResponseEntity.ok(event.getCategories()))
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    // POST /admin/events/{eventID}/categories — назначение направления мероприятию.
    @PostMapping
    public ResponseEntity<Event> assignCategoryToEvent(@PathVariable("eventID") UUID eventID,
                                                       @RequestBody CategoryAssignmentDTO assignmentDTO) {
        return eventRepository.findById(eventID)
                .flatMap(event -> categoryRepository.findById(assignmentDTO.getCategoryId())
                        .map(category -> {
                            event.getCategories().add(category);
                            Event updated = eventRepository.save(event);
                            return ResponseEntity.ok(updated);
                        }))
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    // DELETE /admin/events/{eventID}/categories/{categoryID} — удаление направления из мероприятия.
    @DeleteMapping("/{categoryID}")
    public ResponseEntity<Event> removeCategoryFromEvent(@PathVariable("eventID") UUID eventID,
                                                         @PathVariable("categoryID") UUID categoryID) {
        return (ResponseEntity<Event>) eventRepository.findById(eventID)
                .map(event -> {
                    boolean removed = event.getCategories().removeIf(cat -> cat.getId().equals(categoryID));
                    if (removed) {
                        Event updated = eventRepository.save(event);
                        return ResponseEntity.ok(updated);
                    } else {
                        return ResponseEntity.<Event>status(HttpStatus.BAD_REQUEST).build();
                    }
                })
                .orElseGet(() -> ResponseEntity.<Event>notFound().build());
    }
}