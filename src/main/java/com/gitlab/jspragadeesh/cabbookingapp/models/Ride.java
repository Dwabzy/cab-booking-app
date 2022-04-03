package com.gitlab.jspragadeesh.cabbookingapp.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Data
@NoArgsConstructor
public class Ride {
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    private static class Amount {
        private double rideFare;
        private double tip;
        private double total;
    }

    @Id
    private String id;
    @DBRef
    private User customer;
    @DBRef
    private User driver;
    private Coordinates pickupAddress;
    private Coordinates dropAddress;
    private Date pickUpTime;
    private Date dropTime;
    private Date bookingTime;
    private Integer noOfPassengers;
    public Double distance;
    private String status;
    private Amount rideAmount;
    private CustomerRidePreferences customerRidePreferences;
    @DBRef private Vehicle vehicle;

    public Ride(User customer, Coordinates pickupAddress, Coordinates dropAddress, Double distance, Integer noOfPassengers, CustomerRidePreferences customerRidePreferences) {
        this.id = UUID.randomUUID().toString();
        this.customer = customer;
        this.pickupAddress = pickupAddress;
        this.dropAddress = dropAddress;
        this.noOfPassengers = noOfPassengers;
        this.customerRidePreferences = customerRidePreferences;
        this.distance = distance;
        this.bookingTime = new Date();
        this.status = "PENDING";
        this.rideAmount = new Amount();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Ride ride = (Ride) o;

        return this.id.equals(ride.id);
    }

    // Override hashCode() to prevent duplicate rides
    @Override
    public int hashCode() {
        return this.id.hashCode();
    }
}