package com.t_educational.t_edu_events.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.t_educational.t_edu_events.model.game.GameImplementation;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MapsId;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "event_category_games")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class EventCategoryGame {

    @EmbeddedId
    private EventCategoryGameId id;

    @ManyToOne
    @MapsId("eventId")
    @JoinColumn(name = "event_id")
    @JsonIgnore
    private Event event;

    @ManyToOne
    @MapsId("categoryId")
    @JoinColumn(name = "category_id")
    @JsonIgnore
    private Category category;

    @ManyToOne
    @MapsId("gameId")
    @JoinColumn(name = "game_id")
    private GameImplementation game;

    @JsonProperty("event")
    public Object getEventId() {
        return event != null ? event.getId() : null;
    }

    @JsonProperty("category")
    public Object getCategoryId() {
        return category != null ? category.getId() : null;
    }
}
