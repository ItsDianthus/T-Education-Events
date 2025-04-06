package com.t_educational.t_edu_events.game.quiz;

import jakarta.persistence.*;
import lombok.*;
import java.util.UUID;

@Entity
@Table(name = "quiz_configurations")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class QuizConfigEntity {

    @Id
    @GeneratedValue
    @Column(columnDefinition = "uuid", updatable = false, nullable = false)
    private UUID configId;

    @Convert(converter = QuizConfigConverter.class)
    @Column(name = "config_data", columnDefinition = "text", nullable = false)
    private QuizConfig configData;
}
