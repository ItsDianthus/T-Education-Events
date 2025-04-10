package com.t_educational.t_edu_events.repository.game;

import com.t_educational.t_edu_events.dto.UserStatsDTO;
import com.t_educational.t_edu_events.model.game.GameSession;
import com.t_educational.t_edu_events.model.game.GameSessionStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Collection;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface GameSessionRepository extends JpaRepository<GameSession, UUID> {
    Optional<GameSession> findByUserIdAndGameIdAndStatusIn(UUID userId, UUID gameId, Collection<GameSessionStatus> statuses);
    List<GameSession> findByUserIdAndGameId(UUID userId, UUID gameId);
    List<GameSession> findByUserIdAndGameIdAndEventIdAndCategoryId(
            UUID userId,
            UUID gameId,
            UUID eventId,
            UUID categoryId
    );
    Optional<GameSession> findTopByUserIdAndGameIdOrderByStartTimeDesc(
            UUID userId,
            UUID gameId
    );

    @Query("SELECT new com.t_educational.t_edu_events.dto.UserStatsDTO(gs.userId, SUM(gs.points)) " +
            "FROM GameSession gs " +
            "WHERE gs.eventId = :eventId AND gs.categoryId = :categoryId " +
            "GROUP BY gs.userId " +
            "ORDER BY SUM(gs.points) DESC")
    List<UserStatsDTO> findTopUsersByEventIdAndCategoryId(@Param("eventId") UUID eventId,
                                                          @Param("categoryId") UUID categoryId);


}
