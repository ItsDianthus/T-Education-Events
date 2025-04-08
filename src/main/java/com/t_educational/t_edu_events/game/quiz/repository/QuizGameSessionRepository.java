package com.t_educational.t_edu_events.game.quiz.repository;

import com.t_educational.t_edu_events.game.quiz.model.QuizGameSession;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface QuizGameSessionRepository extends JpaRepository<QuizGameSession, UUID> {
    Optional<QuizGameSession> findTopByUserIdAndGameIdOrderBySessionIdDesc(UUID userId, UUID gameId);
    Optional<QuizGameSession> findByUserIdAndGameId(UUID userId, UUID gameId);
    Optional<QuizGameSession> findTopByUserIdAndGameIdOrderByCreatedAtDesc(UUID userId, UUID gameId);
}
