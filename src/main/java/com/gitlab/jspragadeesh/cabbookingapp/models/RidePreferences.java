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

    public boolean doesRideSatisfyPreferences(Ride ride){
        if(!ride.getCustomerRidePreferences().getPaymentMethod().equals(this.paymentMethod))
            return false;
        if(!ride.getCustomerRidePreferences().getRideExperience().equals(this.rideExperience))
            return false;
        if(!ride.getCustomerRidePreferences().getGender().equals(this.gender))
            return false;
        if(!(ride.getCustomer().getRating() > this.rating))
            return false;
        if(!(ride.getCustomer().getRatingCount() > this.ratingCount))
            return false;
        return true;
    }
}