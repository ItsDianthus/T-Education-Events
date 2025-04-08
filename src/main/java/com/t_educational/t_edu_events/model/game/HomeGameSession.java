package com.t_educational.t_edu_events.model.game;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "home_game_sessions")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class HomeGameSession {

    @Id
    @GeneratedValue
    @Column(columnDefinition = "uuid", updatable = false, nullable = false)
    private UUID sessionId;

    @Column(name = "user_id", nullable = false)
    private UUID userId;

    @Column(name = "game_id", nullable = false)
    private UUID gameId;

    @Column(name = "status", nullable = false)
    @Enumerated(EnumType.STRING)
    private GameSessionStatus status;

    @Column(name = "points", nullable = false)
    private int points;

    @Column(name = "start_time", nullable = false)
    private LocalDateTime startTime;

    @Column(name = "last_update", nullable = false)
    private LocalDateTime lastUpdate;
}
