package com.gitlab.jspragadeesh.cabbookingapp.controllers;

import com.gitlab.jspragadeesh.cabbookingapp.models.DriverRidePreferences;
import com.gitlab.jspragadeesh.cabbookingapp.models.Ride;
import com.gitlab.jspragadeesh.cabbookingapp.models.User;
import com.gitlab.jspragadeesh.cabbookingapp.models.Vehicle;
import com.gitlab.jspragadeesh.cabbookingapp.repositories.NodeRepository;
import com.gitlab.jspragadeesh.cabbookingapp.repositories.RideRepository;
import com.gitlab.jspragadeesh.cabbookingapp.repositories.UserRepository;
import com.gitlab.jspragadeesh.cabbookingapp.repositories.VehicleRepository;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@CrossOrigin
@RequestMapping("api/driver")
@PreAuthorize("hasRole('DRIVER')")
public class DriverController {

    private final UserRepository userRepository;
    private final VehicleRepository vehicleRepository;
    private final RideRepository rideRepository;
    private final NodeRepository nodeRepository;

    public DriverController(UserRepository userRepository, VehicleRepository vehicleRepository, RideRepository rideRepository, NodeRepository nodeRepository) {
        this.userRepository = userRepository;
        this.vehicleRepository = vehicleRepository;
        this.rideRepository = rideRepository;
        this.nodeRepository = nodeRepository;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    private static class AcceptRideRequest{
        private String rideID;

    }

    @PutMapping("/preferences")
    public @ResponseBody
    ResponseEntity<?> updatePreferences(Authentication authentication, @RequestBody DriverRidePreferences preferences) {
        User currentUser = userRepository.findByEmail(authentication.getName());
        currentUser.setDriverRidePreferences(preferences);
        userRepository.save(currentUser);
        return ResponseEntity.ok("Preferences updated successfully");
    }

    // Accept Ride
    @PutMapping("/accept")
    public @ResponseBody
    ResponseEntity<?> acceptRide(Authentication authentication, @RequestBody AcceptRideRequest acceptRideRequest) {
        User currentUser = userRepository.findByEmail(authentication.getName());
        Optional<Ride> rideOptional = rideRepository.findById(acceptRideRequest.getRideID());
        if(!rideOptional.isPresent()) {
            return ResponseEntity.badRequest().body("Ride not found");
        }
        Ride ride = rideOptional.get();
        ride.setDriver(currentUser);
        ride.setStatus("ACCEPTED");
        Optional<Vehicle> vehicleOptional = vehicleRepository.findByDriverId(currentUser.getId());
        if(!vehicleOptional.isPresent()) {
            return ResponseEntity.badRequest().body("Vehicle not found");
        }
        Vehicle vehicle = vehicleOptional.get();
        ride.setVehicle(vehicle);

        // Remove ride from all the nodes in the graph
        nodeRepository.findAll().forEach(node -> {
            node.getRides().remove(ride);
            nodeRepository.save(node);
        });

        rideRepository.save(ride);
        return ResponseEntity.ok("Ride accepted successfully");
    }

    @PutMapping("/end")
    public @ResponseBody
    ResponseEntity<?> endRide(Authentication authentication, @RequestBody AcceptRideRequest acceptRideRequest) {
        Optional<Ride> rideOptional = rideRepository.findById(acceptRideRequest.getRideID());
        if(!rideOptional.isPresent()) {
            return ResponseEntity.badRequest().body("Ride not found");
        }
        Ride ride = rideOptional.get();
        ride.setStatus("COMPLETED");
        rideRepository.save(ride);
        return ResponseEntity.ok("Ride ended successfully");
    }
}
