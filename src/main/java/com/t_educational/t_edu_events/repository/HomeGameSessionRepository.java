package com.t_educational.t_edu_events.repository;

import com.t_educational.t_edu_events.model.game.GameSession;
import com.t_educational.t_edu_events.model.game.HomeGameSession;
import com.t_educational.t_edu_events.model.game.GameSessionStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;
import java.util.Optional;

@Repository
public interface HomeGameSessionRepository extends JpaRepository<HomeGameSession, UUID> {
    Optional<HomeGameSession> findTopByUserIdAndGameIdOrderByStartTimeDesc(UUID userId, UUID gameId);
    List<HomeGameSession> findByUserIdAndGameId(UUID userId, UUID gameId);
}
