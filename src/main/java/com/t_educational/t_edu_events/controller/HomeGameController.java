package com.t_educational.t_edu_events.controller;

import com.t_educational.t_edu_events.model.game.GameImplementation;
import com.t_educational.t_edu_events.model.game.GameSessionStatus;
import com.t_educational.t_edu_events.model.game.HomeGameSession;
import com.t_educational.t_edu_events.repository.GameImplementationRepository;
import com.t_educational.t_edu_events.repository.HomeGameSessionRepository;
import com.t_educational.t_edu_events.repository.UserRepository;
import com.t_educational.t_edu_events.game.quiz.service.QuizGameService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/api/home/games")
@RequiredArgsConstructor
public class HomeGameController {

    private final GameImplementationRepository gameImplementationRepository;
    private final HomeGameSessionRepository homeGameSessionRepository;
    private final QuizGameService quizGameService;
    private final UserRepository userRepository;

    @GetMapping
    public ResponseEntity<List<GameImplementation>> getHomeGames() {
        List<GameImplementation> homeGames = gameImplementationRepository.findByHomeEnableTrue();
        return ResponseEntity.ok(homeGames);
    }

    // POST /api/home/games/{gameId}/start — запуск игровой сессии для домашней игры
    @PostMapping("/{gameId}/start")
    public ResponseEntity<String> startHomeGameSession(@PathVariable UUID gameId,
                                                       Principal principal) {
        String email = principal.getName();
        UUID userId = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found")).getId();

        GameImplementation game = gameImplementationRepository.findById(gameId)
                .orElseThrow(() -> new RuntimeException("Game not found"));

        if (!game.isHomeEnable()) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Game is not available in home mode");
        }

        List<HomeGameSession> existingSessions = homeGameSessionRepository.findByUserIdAndGameId(
                userId, gameId
        );
        if (!existingSessions.isEmpty()) {
            return ResponseEntity.badRequest().body("Session already exists");
        }

        HomeGameSession session = new HomeGameSession();
        session.setUserId(userId);
        session.setGameId(gameId);
        session.setStatus(GameSessionStatus.IN_PROGRESS);
        session.setPoints(0);
        session.setStartTime(LocalDateTime.now());
        session.setLastUpdate(LocalDateTime.now());
        HomeGameSession savedSession = homeGameSessionRepository.save(session);

        if ("QUIZ".equalsIgnoreCase(game.getEngineType())) {
            String engineData = quizGameService.startSession(
                    game.getConfigReference(), userId, gameId, savedSession.getSessionId().toString()
            );
            return ResponseEntity.ok(engineData);
        } else {
            return ResponseEntity.badRequest().body("Unsupported game engine");
        }
    }

    // POST /api/home/games/{gameId}/finish — завершение игровой сессии для домашней игры
    @PostMapping("/{gameId}/finish")
    public ResponseEntity<HomeGameSession> finishHomeGameSession(
            @PathVariable UUID gameId,
            Principal principal) {

        String email = principal.getName();
        UUID userId = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"))
                .getId();


        Optional<HomeGameSession> optionalSession = homeGameSessionRepository
                .findTopByUserIdAndGameIdOrderByStartTimeDesc(userId, gameId);
        if (optionalSession.isEmpty()) {
            return ResponseEntity.badRequest().build();
        }
        HomeGameSession session = optionalSession.get();

        int totalPoints = quizGameService.finishSession(userId, gameId);

        session.setPoints(totalPoints);
        session.setStatus(GameSessionStatus.FINISHED);
        session.setLastUpdate(LocalDateTime.now());
        HomeGameSession savedSession = homeGameSessionRepository.save(session);

        return ResponseEntity.ok(savedSession);
    }

}
