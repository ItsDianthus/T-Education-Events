package com.t_educational.t_edu_events.controller.admin;

import com.t_educational.t_edu_events.model.game.GameImplementation;
import com.t_educational.t_edu_events.repository.*;
import com.t_educational.t_edu_events.model.*;
import com.t_educational.t_edu_events.repository.EventCategoryGameRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/admin/events/{eventId}/categories/{categoryId}/games")
@RequiredArgsConstructor
public class EventCategoryGameController {

    private final EventRepository eventRepository;
    private final CategoryRepository categoryRepository;
    private final GameImplementationRepository gameImplementationRepository;
    private final EventCategoryGameRepository eventCategoryGameRepository;

    // GET /admin/events/{eventId}/categories/{categoryId}/games
    @GetMapping
    public ResponseEntity<List<EventCategoryGame>> getGamesByEventAndCategory(
            @PathVariable UUID eventId,
            @PathVariable UUID categoryId) {

        List<EventCategoryGame> games = eventCategoryGameRepository.findAllByIdEventIdAndIdCategoryId(eventId, categoryId);
        return ResponseEntity.ok(games);
    }

    @PostMapping
    public ResponseEntity<EventCategoryGame> addGameToEventCategory(
            @PathVariable UUID eventId,
            @PathVariable UUID categoryId,
            @RequestBody GameIdRequest request) {

        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new RuntimeException("Event not found"));
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new RuntimeException("Category not found"));
        GameImplementation game = gameImplementationRepository.findById(request.getGameId())
                .orElseThrow(() -> new RuntimeException("Game not found"));

        EventCategoryGameId id = new EventCategoryGameId(eventId, categoryId, request.getGameId());
        if (eventCategoryGameRepository.existsById(id)) {
            return ResponseEntity.badRequest().build();
        }

        EventCategoryGame ecg = new EventCategoryGame();
        ecg.setId(id);
        ecg.setEvent(event);
        ecg.setCategory(category);
        ecg.setGame(game);

        EventCategoryGame saved = eventCategoryGameRepository.save(ecg);
        return ResponseEntity.ok(saved);
    }


    // DELETE /admin/events/{eventId}/categories/{categoryId}/games/{gameId}
    @DeleteMapping("/{gameId}")
    public ResponseEntity<Void> removeGameFromEventCategory(
            @PathVariable UUID eventId,
            @PathVariable UUID categoryId,
            @PathVariable UUID gameId) {

        EventCategoryGameId id = new EventCategoryGameId(eventId, categoryId, gameId);
        if (!eventCategoryGameRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        eventCategoryGameRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    public static class GameIdRequest {
        private UUID gameId;

        public UUID getGameId() {
            return gameId;
        }

        public void setGameId(UUID gameId) {
            this.gameId = gameId;
        }
    }
}
