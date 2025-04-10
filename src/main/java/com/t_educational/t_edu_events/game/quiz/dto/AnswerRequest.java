package com.t_educational.t_edu_events.game.quiz.dto;

import lombok.Data;


@Data
public class AnswerRequest {
    private String answer;

    public AnswerRequest(String answer) {
        this.answer = answer;
    }
}