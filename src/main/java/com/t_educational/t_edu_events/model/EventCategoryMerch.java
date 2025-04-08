package com.t_educational.t_edu_events.model;

import jakarta.persistence.*;
import lombok.*;
import java.util.UUID;

@Entity
@Table(name = "event_categories_merch")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class EventCategoryMerch {

    @Id
    @GeneratedValue
    @Column(columnDefinition = "uuid", updatable = false, nullable = false)
    private UUID id;

    @Column(name = "event_id", nullable = false)
    private UUID eventId;

    @Column(name = "category_id", nullable = false)
    private UUID categoryId;

    @Column(name = "item_name", nullable = false)
    private String itemName;

    @Column(name = "required_points", nullable = false)
    private int requiredPoints;

    @Column(name = "is_available", nullable = false)
    private boolean isAvailable;
}
