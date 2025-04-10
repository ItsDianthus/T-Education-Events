package com.t_educational.t_edu_events.controller;

import com.t_educational.t_edu_events.game.quiz.service.QuizGameService;
import com.t_educational.t_edu_events.model.game.GameImplementation;
import com.t_educational.t_edu_events.model.game.GameSession;
import com.t_educational.t_edu_events.model.game.GameSessionStatus;
import com.t_educational.t_edu_events.repository.GameImplementationRepository;
import com.t_educational.t_edu_events.repository.GameSessionRepository;
import com.t_educational.t_edu_events.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.security.Principal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/api/events/{eventId}/categories/{categoryId}/games/{gameId}")
@RequiredArgsConstructor
public class GameSessionController {

    private final GameImplementationRepository gameImplementationRepository;
    private final GameSessionRepository gameSessionRepository;
    private final QuizGameService quizGameService;
    private final UserRepository userRepository;

     // POST /api/events/{eventId}/categories/{categoryId}/games/{gameId}/start - запуск игровой сессии
     @PostMapping("/start")
     public ResponseEntity<String> startGameSession(
             @PathVariable UUID eventId,
             @PathVariable UUID categoryId,
             @PathVariable UUID gameId,
             Principal principal) {

         String email = principal.getName();
         UUID userId = userRepository.findByEmail(email)
                 .orElseThrow(() -> new RuntimeException("User not found")).getId();

         GameImplementation game = gameImplementationRepository.findById(gameId)
                 .orElseThrow(() -> new RuntimeException("Game not found"));

         List<GameSession> existingSessions = gameSessionRepository.findByUserIdAndGameIdAndEventIdAndCategoryId(
                 userId, gameId, eventId, categoryId
         );
         if (!existingSessions.isEmpty()) {
             return ResponseEntity.badRequest().body("Session already exists");
         }

         GameSession session = new GameSession();
         session.setUserId(userId);
         session.setGameId(gameId);
         session.setEventId(eventId);
         session.setCategoryId(categoryId);
         session.setStatus(GameSessionStatus.IN_PROGRESS);
         session.setPoints(0);
         session.setStartTime(LocalDateTime.now());
         session.setLastUpdate(LocalDateTime.now());
         GameSession savedSession = gameSessionRepository.save(session);

         if ("QUIZ".equalsIgnoreCase(game.getEngineType())) {
             String engineData = quizGameService.startSession(
                     game.getConfigReference(),
                     userId,
                     gameId,
                     savedSession.getSessionId().toString()
             );
             return ResponseEntity.ok(engineData);
         } else {
             return ResponseEntity.badRequest().body("Unsupported game engine");
         }
     }

    // POST /api/events/{eventId}/categories/{categoryId}/games/{gameId}/finish - завершение сессии
    @PostMapping("/finish")
    public ResponseEntity<GameSession> finishGameSession(
            @PathVariable UUID eventId,
            @PathVariable UUID categoryId,
            @PathVariable UUID gameId,
            Principal principal) {

        String email = principal.getName();
        UUID userId = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"))
                .getId();

        Optional<GameSession> optionalSession = gameSessionRepository
                .findTopByUserIdAndGameIdOrderByStartTimeDesc(userId, gameId);
        if (optionalSession.isEmpty()) {
            return ResponseEntity.badRequest().build();
        }
        GameSession session = optionalSession.get();

        int totalPoints = quizGameService.finishSession(userId, gameId);

        session.setPoints(totalPoints);
        session.setStatus(GameSessionStatus.FINISHED);
        session.setLastUpdate(LocalDateTime.now());
        GameSession savedSession = gameSessionRepository.save(session);

        return ResponseEntity.ok(savedSession);
    }


}
