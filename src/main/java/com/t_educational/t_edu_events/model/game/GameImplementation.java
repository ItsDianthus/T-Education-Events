package com.t_educational.t_edu_events.model.game;

import com.t_educational.t_edu_events.model.Category;
import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Entity
@Table(name = "game_implementations")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class GameImplementation {

    @Id
    @GeneratedValue
    @Column(columnDefinition = "uuid", updatable = false, nullable = false)
    private UUID gameId;

    @Column(nullable = false)
    private String engineType;  // пусть будет "QUIZ" или "HANGMAN"

    @Column(nullable = false)
    private String configReference; // Будет внешним после вынесения игр в микросервисы

    @Column(nullable = false)
    private String name;

    @Column(columnDefinition = "text")
    private String description;

    @Column(nullable = false)
    private boolean isHomeEnable;

    @ManyToMany
    @JoinTable(
            name = "game_implementation_categories",
            joinColumns = @JoinColumn(name = "game_id"),
            inverseJoinColumns = @JoinColumn(name = "category_id")
    )
    private Set<Category> categories = new HashSet<>();
}
