package com.gitlab.jspragadeesh.cabbookingapp.models.responses;

import com.gitlab.jspragadeesh.cabbookingapp.models.CustomerRidePreferences;
import com.gitlab.jspragadeesh.cabbookingapp.models.DriverRidePreferences;
import com.gitlab.jspragadeesh.cabbookingapp.models.Location;
import com.gitlab.jspragadeesh.cabbookingapp.models.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserResponse {
    private String userId;
    private String firstName;
    private String lastName;
    private String gender;
    private List<String> phoneNumbers;
    private String email;
    private Set<String> roles;
    private List<Location> locations;
    private Double rating;
    private Integer ratingCount;
    private CustomerRidePreferences customerRidePreferences;
    private DriverRidePreferences driverRidePreferences;
    private Double walletBalance;

    public UserResponse(User user) {
        // Initialise all the fields
        this.userId = user.getId();
        this.firstName = user.getFirstName();
        this.lastName = user.getLastName();
        this.gender = user.getGender();
        this.phoneNumbers = user.getPhoneNumbers();
        this.email = user.getEmail();
        this.roles = user.getRoles();
        this.locations = user.getLocations();
        this.rating = user.getRating();
        this.ratingCount = user.getRatingCount();
        this.customerRidePreferences = user.getCustomerRidePreferences();
        this.driverRidePreferences = user.getDriverRidePreferences();
        this.walletBalance = user.getWalletBalance();
    }
}
