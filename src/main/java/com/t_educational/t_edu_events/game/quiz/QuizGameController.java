package com.t_educational.t_edu_events.game.quiz;

import com.t_educational.t_edu_events.model.GameSession;
import com.t_educational.t_edu_events.service.GameSessionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.UUID;

@RestController
@RequestMapping("/api/quiz")
@RequiredArgsConstructor
public class QuizGameController {

    private final GameSessionService gameSessionService;

    @PostMapping("/start")
    public ResponseEntity<GameSession> startSession(Principal principal) {
        UUID userId = UUID.fromString(principal.getName()); // предположим, principal содержит userId
        UUID gameId = UUID.randomUUID(); // или получить из контекста игры
        String engineData = "{}"; // начальное состояние (например, пустой JSON)
        GameSession session = gameSessionService.startSession(userId, gameId, engineData);
        return ResponseEntity.ok(session);
    }

    @PutMapping("/session/{sessionId}")
    public ResponseEntity<GameSession> updateSession(@PathVariable UUID sessionId,
                                                     @RequestBody UpdateSessionRequest request) {
        GameSession updatedSession = gameSessionService.updateSession(sessionId, request.getAdditionalPoints(), request.getEngineData());
        return ResponseEntity.ok(updatedSession);
    }

    @PostMapping("/finish")
    public ResponseEntity<GameSession> finishSession(@RequestBody FinishSessionRequest request) {
        GameSession finishedSession = gameSessionService.finishSession(request.getSessionId());
        return ResponseEntity.ok(finishedSession);
    }

    public static class UpdateSessionRequest {
        private int additionalPoints;
        private String engineData;

        public int getAdditionalPoints() {
            return additionalPoints;
        }

        public void setAdditionalPoints(int additionalPoints) {
            this.additionalPoints = additionalPoints;
        }

        public String getEngineData() {
            return engineData;
        }

        public void setEngineData(String engineData) {
            this.engineData = engineData;
        }
    }

    public static class FinishSessionRequest {
        private UUID sessionId;

        public UUID getSessionId() {
            return sessionId;
        }

        public void setSessionId(UUID sessionId) {
            this.sessionId = sessionId;
        }
    }
}
