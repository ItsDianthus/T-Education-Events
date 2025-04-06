package com.t_educational.t_edu_events.game.quiz;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class QuizGameSession {
    private UUID sessionId;
    private UUID userId;
    private UUID gameId;
    private int totalPoints;
    private int currentQuestionIndex;
    private LocalDateTime startTime;
    private LocalDateTime lastUpdate;
    private SessionStatus status;

    private String engineData;

    public enum SessionStatus {
        NOT_STARTED,
        IN_PROGRESS,
        FINISHED
    }
}
