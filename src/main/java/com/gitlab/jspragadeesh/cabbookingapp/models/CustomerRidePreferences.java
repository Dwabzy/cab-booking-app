package com.gitlab.jspragadeesh.cabbookingapp.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CustomerRidePreferences extends RidePreferences {
    private String vehicleType; // comfortable | eco friendly | luxury
    private String vehicle; // small | medium | large
    private Double rideCount; // number of rides
}
