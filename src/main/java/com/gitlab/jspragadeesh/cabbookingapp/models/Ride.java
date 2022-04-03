package com.gitlab.jspragadeesh.cabbookingapp.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;

import java.util.Date;

@Data
@NoArgsConstructor
public class Ride {
    private static class RideID {
        @Indexed
        private String userId;
        @Indexed
        private String vehicleId;

        public RideID(String userId, String vehicleId) {
            this.userId = userId;
            this.vehicleId = vehicleId;
        }
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    private static class Amount {
        private double rideFare;
        private double tip;
        private double total;
    }

    @Id
    private RideID id;
    private Address pickupAddress;
    private Address dropAddress;
    private Date pickUpTime;
    private Date dropTime;
    private String status;
    private Amount rideAmount;

    public Ride(String userId, String vehicleId, Address pickupAddress, Address dropAddress, Date pickUpTime, Date dropTime) {
        this.id = new RideID(userId, vehicleId);
        this.pickupAddress = pickupAddress;
        this.dropAddress = dropAddress;
        this.pickUpTime = pickUpTime;
        this.dropTime = dropTime;
        this.status = "PENDING";
        this.rideAmount = new Amount();
    }
}
