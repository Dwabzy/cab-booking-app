package com.gitlab.jspragadeesh.cabbookingapp.models.responses;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RideResponse {
    private String rideId;
    private String status;
    private Double rideFare;
    private Date currentTime;
}
