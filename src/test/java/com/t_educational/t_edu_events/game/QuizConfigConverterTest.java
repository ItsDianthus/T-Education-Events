package com.t_educational.t_edu_events.game;

import static org.junit.jupiter.api.Assertions.*;

import com.t_educational.t_edu_events.game.quiz.service.QuizConfigConverter;
import com.t_educational.t_edu_events.game.quiz.model.QuizConfig;
import com.t_educational.t_edu_events.game.quiz.model.QuizConfig.Question;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.List;

class QuizConfigConverterTest {

    private QuizConfigConverter converter;

    @BeforeEach
    void setUp() {
        converter = new QuizConfigConverter();
    }

    @Test
    void testConvertToDatabaseColumnAndBack() {
        Question question = new Question("What is the capital of France?",
                List.of("Paris", "London", "Berlin"),
                List.of("Paris"),
                10);
        QuizConfig original = new QuizConfig(List.of(question));

        String json = converter.convertToDatabaseColumn(original);
        assertNotNull(json);
        assertTrue(json.contains("What is the capital of France?"));

        QuizConfig converted = converter.convertToEntityAttribute(json);
        assertNotNull(converted);
        assertEquals(original, converted);
    }

    @Test
    void testConvertToDatabaseColumnWithNull() {
        String json = converter.convertToDatabaseColumn(null);
        assertEquals("null", json);
    }

    @Test
    void testConvertToEntityAttributeWithNull() {
        QuizConfig config = converter.convertToEntityAttribute(null);
        assertNull(config);
    }

    @Test
    void testConvertToEntityAttributeInvalidJson() {
        String invalidJson = "{invalid json}";
        Exception exception = assertThrows(IllegalArgumentException.class, () ->
                converter.convertToEntityAttribute(invalidJson));
        String expectedMessage = "Error converting JSON to QuizConfig";
        assertTrue(exception.getMessage().contains(expectedMessage));
    }
}
