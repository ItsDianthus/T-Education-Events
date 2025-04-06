package com.t_educational.t_edu_events.game.quiz;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class QuizConfig {
    private List<Question> questions;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Question {
        private String questionText;
        private List<String> possibleAnswers;
        private List<String> correctAnswers;
        private int points;
        // Мб лимит времени
    }
}
