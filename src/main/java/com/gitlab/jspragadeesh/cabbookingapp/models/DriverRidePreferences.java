package com.gitlab.jspragadeesh.cabbookingapp.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DriverRidePreferences extends RidePreferences {
    private Coordinates dropLocation;
    private Double dropLocationRadius;
    private Double rideDistance;
    private Double rideDistanceRadius;
    private Double minimumRideFare;

//    public boolean doesRideSatisfyDriversPreferences(DriverRidePreferences ridePreferences) {
//        // Check if Destination is within droplocationRadius km of the preferred DropLocation
//        if(this.getDropAddress().getDistance(ridePreferences.getDropLocation()) > ridePreferences.getDropLocationRadius()) {
//            return false;
//        }
//        // Check if distance is more or less than the preferred distance range
//        if(this.getDistance() > ridePreferences.getRideDistance() + ridePreferences.getRideDistanceRadius()) {
//            return false;
//        }
//        if(this.getDistance() < ridePreferences.getRideDistance() - ridePreferences.getRideDistanceRadius()) {
//            return false;
//        }
//
//        // Check if estimatedRideFare is less than the preferred rideFare
//        if(this.getEstimatedRideAmount() > ridePreferences.getMinimumRideFare()) {
//            return false;
//        }
//        return true;
//    }

    @Override
    public boolean doesRideSatisfyPreferences(Ride ride) {

        // Check if Destination is within droplocationRadius km of the preferred DropLocation
        if(ride.getDropAddress().getDistance(this.getDropLocation()) > this.getDropLocationRadius()) {
            return false;
        }
        // Check if distance is more or less than the preferred distance range
        if(ride.getDistance() > this.getRideDistance() + this.getRideDistanceRadius()) {
            return false;
        }
        if(ride.getDistance() < this.getRideDistance() - this.getRideDistanceRadius()) {
            return false;
        }

        // Check if estimatedRideFare is less than the preferred rideFare
        if(ride.getEstimatedRideAmount() > this.getMinimumRideFare()) {
            return false;
        }
        return super.doesRideSatisfyPreferences(ride);
    }
}