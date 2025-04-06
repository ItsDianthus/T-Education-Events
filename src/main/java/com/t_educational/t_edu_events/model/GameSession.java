package com.t_educational.t_edu_events.model;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.web.bind.support.SessionStatus;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "game_sessions")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class GameSession {

    @Id
    @GeneratedValue
    @Column(columnDefinition = "uuid", updatable = false, nullable = false)
    private UUID sessionId;

    // Идентификатор пользователя, начинавшего игровую сессию
    @Column(nullable = false)
    private UUID userId;

    // Идентификатор игровой реализации из общего реестра игр
    @Column(nullable = false)
    private UUID gameId;

    // Общее количество баллов, заработанных в сессии
    @Column(nullable = false)
    private int totalPoints;

    // Статус сессии: NOT_STARTED, IN_PROGRESS, FINISHED
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private GameSessionStatus status;

    // Время начала сессии
    @Column(nullable = false)
    private LocalDateTime startTime;

    // Время последнего обновления сессии
    @Column(nullable = false)
    private LocalDateTime lastUpdate;

    // Специфичные данные игрового движка в формате JSON (например, текущий этап, состояние игры и т.д.)
    @Column(columnDefinition = "jsonb")
    private String engineData;
}
