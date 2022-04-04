package com.gitlab.jspragadeesh.cabbookingapp.models.requests;

import com.gitlab.jspragadeesh.cabbookingapp.models.Address;
import com.gitlab.jspragadeesh.cabbookingapp.models.CustomerRidePreferences;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RideRequest{
    private Address pickupAddress;
    private Address dropAddress;
    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private Integer noOfPassengers;
    private CustomerRidePreferences preferences;
    private String bookingType; // "now" or "later"
    private Date pickUpTime;
}