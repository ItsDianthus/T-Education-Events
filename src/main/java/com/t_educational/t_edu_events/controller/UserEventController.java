package com.t_educational.t_edu_events.controller;

import com.t_educational.t_edu_events.model.Activity;
import com.t_educational.t_edu_events.model.Category;
import com.t_educational.t_edu_events.model.Event;
import com.t_educational.t_edu_events.model.EventCategoryGame;
import com.t_educational.t_edu_events.model.EventCategoryGameId;
import com.t_educational.t_edu_events.model.game.GameImplementation;
import com.t_educational.t_edu_events.repository.ActivityRepository;
import com.t_educational.t_edu_events.repository.EventCategoryGameRepository;
import com.t_educational.t_edu_events.repository.EventRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/events")
@RequiredArgsConstructor
public class UserEventController {

    private final EventRepository eventRepository;
    private final EventCategoryGameRepository eventCategoryGameRepository;
    private final ActivityRepository activityRepository;

    private ResponseEntity<Event> getActiveEvent(UUID eventId) {
        return (ResponseEntity<Event>) eventRepository.findById(eventId)
                .map(event -> {
                    if (!event.isActive()) {
                        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(null);
                    }
                    return ResponseEntity.ok(event);
                })
                .orElseGet(() -> ResponseEntity.notFound().build());
    }


    // GET /api/events - возвращает список активных мероприятий (active == true)
    @GetMapping
    public ResponseEntity<List<Event>> getActiveEvents() {
        List<Event> activeEvents = eventRepository.findByActiveTrue();
        return ResponseEntity.ok(activeEvents);
    }

    // GET /api/events/{eventId}/activities - возвращает список активностей мероприятия
    @GetMapping("/{eventId}/activities")
    public ResponseEntity<List<Activity>> getEventActivities(@PathVariable UUID eventId) {
        ResponseEntity<Event> eventResponse = getActiveEvent(eventId);
        if (!eventResponse.getStatusCode().is2xxSuccessful()) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        List<Activity> activities = activityRepository.findByEventId(eventId);
        return ResponseEntity.ok(activities);
    }

    // GET /api/events/{eventId}/categories - возвращает список направлений (категорий) на мероприятии
    @GetMapping("/{eventId}/categories")
    public ResponseEntity<Iterable<Category>> getEventCategories(@PathVariable UUID eventId) {
        ResponseEntity<Event> eventResponse = getActiveEvent(eventId);
        if (!eventResponse.getStatusCode().is2xxSuccessful()) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        Event event = eventResponse.getBody();
        return ResponseEntity.ok(event.getCategories());
    }

    // GET /api/events/{eventId}/categories/{categoryId}/games - возвращает список игр по выбранному направлению мероприятия
    @GetMapping("/{eventId}/categories/{categoryId}/games")
    public ResponseEntity<List<GameImplementation>> getGamesByCategory(
            @PathVariable UUID eventId,
            @PathVariable UUID categoryId) {
        ResponseEntity<Event> eventResponse = getActiveEvent(eventId);
        if (!eventResponse.getStatusCode().is2xxSuccessful()) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        List<EventCategoryGame> ecgList = eventCategoryGameRepository.findAllByIdEventIdAndIdCategoryId(eventId, categoryId);
        List<GameImplementation> games = ecgList.stream()
                .map(EventCategoryGame::getGame)
                .toList();
        return ResponseEntity.ok(games);
    }

    // GET /api/events/{eventId}/categories/{categoryId}/games/{gameId} - возвращает информацию о конкретной игре
    @GetMapping("/{eventId}/categories/{categoryId}/games/{gameId}")
    public ResponseEntity<GameImplementation> getGameInfo(
            @PathVariable UUID eventId,
            @PathVariable UUID categoryId,
            @PathVariable UUID gameId) {
        ResponseEntity<Event> eventResponse = getActiveEvent(eventId);
        if (!eventResponse.getStatusCode().is2xxSuccessful()) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        EventCategoryGameId compositeId = new EventCategoryGameId(eventId, categoryId, gameId);
        EventCategoryGame ecg = eventCategoryGameRepository.findById(compositeId)
                .orElseThrow(() -> new RuntimeException("Game not found for this event and category"));
        return ResponseEntity.ok(ecg.getGame());
    }
}
