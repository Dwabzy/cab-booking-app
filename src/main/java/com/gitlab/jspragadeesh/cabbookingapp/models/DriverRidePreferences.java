package com.gitlab.jspragadeesh.cabbookingapp.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DriverRidePreferences extends RidePreferences {
    private Coordinates dropLocation;
    private Double rideDistance;
    private Double rideFare;
}