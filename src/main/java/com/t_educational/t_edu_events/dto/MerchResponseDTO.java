package com.t_educational.t_edu_events.dto;

import lombok.Data;
import java.util.List;

@Data
public class MerchResponseDTO {
    private String totalPoints;
    private List<MerchItemDTO> earnedMerch;
    private List<MerchItemDTO> notEarnedMerch;
}
