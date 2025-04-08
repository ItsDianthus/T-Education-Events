package com.t_educational.t_edu_events.controller;


import com.t_educational.t_edu_events.dto.MerchItemDTO;
import com.t_educational.t_edu_events.dto.MerchResponseDTO;
import com.t_educational.t_edu_events.model.Event;
import com.t_educational.t_edu_events.model.EventCategoryGame;
import com.t_educational.t_edu_events.model.EventCategoryMerch;
import com.t_educational.t_edu_events.model.game.GameImplementation;
import com.t_educational.t_edu_events.model.game.GameSession;
import com.t_educational.t_edu_events.repository.EventCategoryGameRepository;
import com.t_educational.t_edu_events.repository.EventCategoryMerchRepository;
import com.t_educational.t_edu_events.repository.EventRepository;
import com.t_educational.t_edu_events.repository.GameSessionRepository;
import com.t_educational.t_edu_events.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/events")
@RequiredArgsConstructor
public class UserMerchController {

    private final EventRepository eventRepository;
    private final EventCategoryMerchRepository eventCategoryMerchRepository;
    private final EventCategoryGameRepository eventCategoryGameRepository;
    private final GameSessionRepository gameSessionRepository;
    private final UserRepository userRepository;

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
    // GET /api/events/{eventId}/categories/{categoryId}/prizes
    // Возвращает информацию о призах (мерче) для выбранного направления с учётом баллов пользователя
    @GetMapping("/{eventId}/categories/{categoryId}/merch")
    public ResponseEntity<MerchResponseDTO> getPrizes(
            @PathVariable UUID eventId,
            @PathVariable UUID categoryId,
            Principal principal) {

        ResponseEntity<Event> eventResponse = getActiveEvent(eventId);
        if (!eventResponse.getStatusCode().is2xxSuccessful()) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        String email = principal.getName();
        UUID userId = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found")).getId();

        List<EventCategoryMerch> merchList = eventCategoryMerchRepository.findByEventIdAndCategoryId(eventId, categoryId);
        List<EventCategoryGame> ecgList = eventCategoryGameRepository.findAllByIdEventIdAndIdCategoryId(eventId, categoryId);
        int totalPoints = 0;

        for (EventCategoryGame ecg : ecgList) {
            GameImplementation game = ecg.getGame();
            List<GameSession> sessions = gameSessionRepository.findByUserIdAndGameId(userId, game.getGameId());
            for (GameSession s : sessions) {
                totalPoints += s.getPoints();
            }
        }

        List<MerchItemDTO> earnedMerch = new ArrayList<>();
        List<MerchItemDTO> notEarnedMerch = new ArrayList<>();
        for (EventCategoryMerch merch : merchList) {
            MerchItemDTO dto = new MerchItemDTO();
            dto.setName(merch.getItemName());
            dto.setPoints(String.valueOf(merch.getRequiredPoints()));
            dto.setAvailable(merch.isAvailable());
            if (totalPoints >= merch.getRequiredPoints()) {
                earnedMerch.add(dto);
            } else {
                notEarnedMerch.add(dto);
            }
        }

        earnedMerch.sort(Comparator.comparingInt(m -> Integer.parseInt(m.getPoints())));
        notEarnedMerch.sort(Comparator.comparingInt(m -> Integer.parseInt(m.getPoints())));

        MerchResponseDTO response = new MerchResponseDTO();
        response.setTotalPoints(String.valueOf(totalPoints));
        response.setEarnedMerch(earnedMerch);
        response.setNotEarnedMerch(notEarnedMerch);

        return ResponseEntity.ok(response);
    }
}
