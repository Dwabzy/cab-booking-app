package com.gitlab.jspragadeesh.cabbookingapp.models;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RidePreferences {
    private String paymentMethod;
    private String rideExperience; //  silent | Any | talkative
    private String gender; // Male | Female | Any
    private Double rating; // 0.0 to 5.0
    private Double ratingCount; // number of ratings
}