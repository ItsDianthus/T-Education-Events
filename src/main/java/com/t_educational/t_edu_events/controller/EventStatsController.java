package com.t_educational.t_edu_events.controller;

import com.t_educational.t_edu_events.dto.UserStatsDTO;
import com.t_educational.t_edu_events.repository.GameSessionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/events/{eventId}/categories/{categoryId}/stats")
@RequiredArgsConstructor
public class EventStatsController {

    private final GameSessionRepository gameSessionRepository;

    // GET /api/events/{eventId}/categories/{categoryId}/stats - топ пользователей по категории
    @GetMapping
    public ResponseEntity<List<UserStatsDTO>> getUserStats(
            @PathVariable UUID eventId,
            @PathVariable UUID categoryId) {
        List<UserStatsDTO> stats = gameSessionRepository.findTopUsersByEventIdAndCategoryId(eventId, categoryId);
        return ResponseEntity.ok(stats);
    }
}
