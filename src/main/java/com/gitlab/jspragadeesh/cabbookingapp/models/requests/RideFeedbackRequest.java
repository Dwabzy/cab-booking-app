package com.gitlab.jspragadeesh.cabbookingapp.models.requests;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RideFeedbackRequest {
    private String rideId;
    private String feedback;
    private Integer rating;
    private Double tip;
}
