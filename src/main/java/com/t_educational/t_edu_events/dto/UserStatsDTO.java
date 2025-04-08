package com.t_educational.t_edu_events.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.UUID;

@Data
@AllArgsConstructor
public class UserStatsDTO {
    private UUID userId;
    private long totalPoints;
}
