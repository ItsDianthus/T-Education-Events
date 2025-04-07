package com.t_educational.t_edu_events.game.quiz.dto;

import lombok.Data;
import java.util.List;

@Data
public class AnswerResponse {
    private String result;
    private int pointsAppended;
    private int currentQuestion;
    private String questionText;
    private List<String> possibleAnswers;
    private int questionPoints;
    private List<String> correctAnswers;
    private int totalPoints;
}
