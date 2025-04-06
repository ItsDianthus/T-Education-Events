package com.t_educational.t_edu_events.model;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.UUID;

@Embeddable
@Data
@NoArgsConstructor
@AllArgsConstructor
public class EventCategoryGameId implements Serializable {

    @Column(name = "event_id", columnDefinition = "uuid")
    private UUID eventId;

    @Column(name = "category_id", columnDefinition = "uuid")
    private UUID categoryId;

    @Column(name = "game_id", columnDefinition = "uuid")
    private UUID gameId;
}
