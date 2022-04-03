package com.gitlab.jspragadeesh.cabbookingapp.models;

import com.gitlab.jspragadeesh.cabbookingapp.models.Coordinates;
import com.gitlab.jspragadeesh.cabbookingapp.models.User;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.DBRef;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
public class Node {
    private Coordinates coordinates;
    @DBRef
    private List<Ride> rides;
    @DBRef
    private List<User> drivers;

    public Node(Coordinates coordinates) {
        this.coordinates = coordinates;
        this.rides = new ArrayList<>();
        this.drivers = new ArrayList<>();
    }

    public void addRide(Ride ride) {
        rides.add(ride);
    }

    public void addDriver(User driver) {
        drivers.add(driver);
    }

    public void removeRide(Ride ride) {
        rides.remove(ride);
    }

    public void removeDriver(User driver) {
        drivers.remove(driver);
    }

    public int getRideCount() {
        return rides.size();
    }

    public int getDriverCount() {
        return drivers.size();
    }

    public boolean areDriversAvailable() {
        return drivers.size() > 0;
    }

    public boolean areRidesAvailable() {
        return rides.size() > 0;
    }
}
