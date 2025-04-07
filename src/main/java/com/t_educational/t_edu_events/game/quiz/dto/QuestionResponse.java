package com.t_educational.t_edu_events.game.quiz.dto;

import lombok.Data;

@Data
public class QuestionResponse {
    private int currentQuestion;
    private Object question;
    private int points;
    private int totalPoints;
}