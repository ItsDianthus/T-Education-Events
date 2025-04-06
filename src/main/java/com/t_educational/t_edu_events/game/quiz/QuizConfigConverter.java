package com.t_educational.t_edu_events.game.quiz;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = true)
public class QuizConfigConverter implements AttributeConverter<QuizConfig, String> {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public String convertToDatabaseColumn(QuizConfig attribute) {
        try {
            return objectMapper.writeValueAsString(attribute);
        } catch (Exception e) {
            throw new IllegalArgumentException("Error converting QuizConfig to JSON", e);
        }
    }

    @Override
    public QuizConfig convertToEntityAttribute(String dbData) {
        try {
            return objectMapper.readValue(dbData, QuizConfig.class);
        } catch (Exception e) {
            throw new IllegalArgumentException("Error converting JSON to QuizConfig", e);
        }
    }
}
