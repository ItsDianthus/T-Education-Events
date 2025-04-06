package com.t_educational.t_edu_events.service;

import com.t_educational.t_edu_events.model.GameSession;
import com.t_educational.t_edu_events.model.GameSessionStatus;
import com.t_educational.t_edu_events.repository.GameSessionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class GameSessionService {

    private final GameSessionRepository gameSessionRepository;

    public GameSession startSession(UUID userId, UUID gameId, String engineData) {
        GameSession session = new GameSession();
        session.setSessionId(UUID.randomUUID());
        session.setUserId(userId);
        session.setGameId(gameId);
        session.setTotalPoints(0);
        session.setStatus(GameSessionStatus.IN_PROGRESS);
        session.setStartTime(LocalDateTime.now());
        session.setLastUpdate(LocalDateTime.now());
        session.setEngineData(engineData);
        return gameSessionRepository.save(session);
    }

    public GameSession updateSession(UUID sessionId, int additionalPoints, String updatedEngineData) {
        GameSession session = gameSessionRepository.findById(sessionId)
                .orElseThrow(() -> new RuntimeException("Сессия не найдена"));
        session.setTotalPoints(session.getTotalPoints() + additionalPoints);
        session.setEngineData(updatedEngineData);
        session.setLastUpdate(LocalDateTime.now());
        return gameSessionRepository.save(session);
    }

    public GameSession finishSession(UUID sessionId) {
        GameSession session = gameSessionRepository.findById(sessionId)
                .orElseThrow(() -> new RuntimeException("Сессия не найдена"));
        session.setStatus(GameSessionStatus.FINISHED);
        session.setLastUpdate(LocalDateTime.now());
        return gameSessionRepository.save(session);
    }
}
