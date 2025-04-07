package com.t_educational.t_edu_events.game.quiz.model;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;
import java.util.UUID;

@Entity
@Table(name = "quiz_game_sessions")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class QuizGameSession {

    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    @Column(name = "session_id", columnDefinition = "uuid", updatable = false, nullable = false)
    private UUID sessionId;

    @Column(name = "user_id", nullable = false)
    private UUID userId;

    @Column(name = "game_id", nullable = false)
    private UUID gameId;

    @Column(name = "game_session_id_reference", columnDefinition = "text", nullable = false)
    private String gameSessionIdReference;

    @Column(name = "current_question", nullable = false)
    private int currentQuestion;

    @Column(name = "total_points", nullable = false)
    private int totalPoints;

    // Новое поле для хранения ID квиз-конфигурации
    @Column(name = "config_id", columnDefinition = "uuid", nullable = false)
    private UUID configId;
}
