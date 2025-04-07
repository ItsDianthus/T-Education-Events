package com.t_educational.t_edu_events.repository;

import com.t_educational.t_edu_events.model.game.GameSession;
import com.t_educational.t_edu_events.model.game.GameSessionStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface GameSessionRepository extends JpaRepository<GameSession, UUID> {
    Optional<GameSession> findByUserIdAndGameIdAndStatusIn(UUID userId, UUID gameId, Collection<GameSessionStatus> statuses);
}
