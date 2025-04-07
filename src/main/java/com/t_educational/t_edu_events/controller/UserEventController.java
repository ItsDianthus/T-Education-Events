package com.t_educational.t_edu_events.controller;

import com.t_educational.t_edu_events.model.Category;
import com.t_educational.t_edu_events.model.Event;
import com.t_educational.t_edu_events.model.game.GameImplementation;
import com.t_educational.t_edu_events.model.EventCategoryGame;
import com.t_educational.t_edu_events.model.EventCategoryGameId;
import com.t_educational.t_edu_events.repository.EventRepository;
import com.t_educational.t_edu_events.repository.EventCategoryGameRepository;
import lombok.RequiredArgsConstructor;
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

     // GET /api/events
     // Возвращает список активных мероприятий (active == true)
    @GetMapping
    public ResponseEntity<List<Event>> getActiveEvents() {
        List<Event> activeEvents = eventRepository.findByActiveTrue();
        return ResponseEntity.ok(activeEvents);
    }

     // GET /api/events/{eventId}/activities - активности мероприятия
    @GetMapping("/{eventId}/activities")
    public ResponseEntity<List<?>> getEventActivities(@PathVariable UUID eventId) {
        // Если у Event есть поле activities, можно вернуть его.
        // Здесь для примера возвращаем пустой список.
        return ResponseEntity.ok(List.of());
    }

    /**
     * GET /api/events/{eventId}/categories
     * Возвращает список направлений (категорий) на мероприятии.
     */
    @GetMapping("/{eventId}/categories")
    public ResponseEntity<Iterable<Category>> getEventCategories(@PathVariable UUID eventId) {
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new RuntimeException("Event not found"));
        return ResponseEntity.ok(event.getCategories());
    }

    /**
     * GET /api/events/{eventId}/categories/{categoryId}/games
     * Возвращает список игр для выбранного направления мероприятия.
     */
    @GetMapping("/{eventId}/categories/{categoryId}/games")
    public ResponseEntity<List<GameImplementation>> getGamesByCategory(
            @PathVariable UUID eventId,
            @PathVariable UUID categoryId) {
        List<EventCategoryGame> ecgList = eventCategoryGameRepository.findAllByIdEventIdAndIdCategoryId(eventId, categoryId);
        // Извлекаем объекты игр из найденных связей
        List<GameImplementation> games = ecgList.stream()
                .map(EventCategoryGame::getGame)
                .toList();
        return ResponseEntity.ok(games);
    }

    /**
     * GET /api/events/{eventId}/categories/{categoryId}/games/{gameId}
     * Возвращает информацию о конкретной игре.
     */
    @GetMapping("/{eventId}/categories/{categoryId}/games/{gameId}")
    public ResponseEntity<GameImplementation> getGameInfo(
            @PathVariable UUID eventId,
            @PathVariable UUID categoryId,
            @PathVariable UUID gameId) {
        EventCategoryGameId compositeId = new EventCategoryGameId(eventId, categoryId, gameId);
        EventCategoryGame ecg = eventCategoryGameRepository.findById(compositeId)
                .orElseThrow(() -> new RuntimeException("Game not found for this event and category"));
        return ResponseEntity.ok(ecg.getGame());
    }
}

