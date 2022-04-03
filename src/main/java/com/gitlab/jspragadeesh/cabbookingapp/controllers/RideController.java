package com.gitlab.jspragadeesh.cabbookingapp.controllers;


import com.gitlab.jspragadeesh.cabbookingapp.models.*;
import com.gitlab.jspragadeesh.cabbookingapp.repositories.NodeRepository;
import com.gitlab.jspragadeesh.cabbookingapp.repositories.RideRepository;
import com.gitlab.jspragadeesh.cabbookingapp.repositories.UserRepository;
import com.gitlab.jspragadeesh.cabbookingapp.repositories.VehicleRepository;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@RestController
@CrossOrigin
@RequestMapping("api/rides")
@PreAuthorize("hasRole('USER')")
public class RideController {

    private final RideRepository rideRepository;
    private final UserRepository userRepository;
    private final NodeRepository nodeRepository;
    private final VehicleRepository vehicleRepository;

    public RideController(RideRepository rideRepository, UserRepository userRepository,
                          NodeRepository nodeRepository, VehicleRepository vehicleRepository) {
        this.rideRepository = rideRepository;
        this.userRepository = userRepository;
        this.nodeRepository = nodeRepository;
        this.vehicleRepository = vehicleRepository;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    private static class RideRequest{
        private Address pickupAddress;
        private Address dropAddress;
        @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
        private Integer noOfPassengers;
        private CustomerRidePreferences preferences;
        private String bookingType; // "now" or "later"
        private Date pickUpTime;
    }

    @PostMapping("/book")

    public @ResponseBody
    ResponseEntity<?> bookRide(Authentication authentication, @RequestBody RideRequest rideRequest){
        User user = userRepository.findByEmail(authentication.getName());

        // Check if the user has already booked a ride
        Optional<Ride> doesRideExist = rideRepository.findByCustomerId(user.getId());
        if(doesRideExist.isPresent()){
            return ResponseEntity.badRequest().body("You have already booked a ride");
        }

        // Generating random coords for pickup and drop addresses for now (Without API)
        Coordinates pickUpCords = new Coordinates(rideRequest.getPickupAddress());
        Coordinates dropCords = new Coordinates(rideRequest.getDropAddress());
        Double distance = pickUpCords.getDistance(dropCords);

        // Get List of Nodes from DB
        List<Node> nodes = nodeRepository.findAll();

        // Get K nearest Nodes
        System.out.println("User " + pickUpCords);
        List<Node> closestNodes = pickUpCords.findNearestNodes(nodes, 3);


        Ride ride = new Ride(
                user,
                pickUpCords,
                dropCords,
                distance,
                rideRequest.getNoOfPassengers(),
                rideRequest.getPreferences()
        );
       // Add User to the nodes
        closestNodes.forEach(node -> node.addRide(ride));
        nodeRepository.saveAll(closestNodes);
        rideRepository.save(ride);
        return null;
    }


    @PostMapping("/find-ride")
    @PreAuthorize("hasRole('DRIVER')")
    public @ResponseBody
    ResponseEntity<?> findRide(Authentication authentication, @RequestBody RideRequest rideRequest){
        User driver = userRepository.findByEmail(authentication.getName());
        Optional<Vehicle> vehicle = vehicleRepository.findByDriverId(driver.getId());
        if(!vehicle.isPresent()){
            return ResponseEntity.badRequest().body("You have not registered a vehicle");
        }

        return null;
    }
}