package com.gitlab.jspragadeesh.cabbookingapp.controllers;


import com.gitlab.jspragadeesh.cabbookingapp.models.*;
import com.gitlab.jspragadeesh.cabbookingapp.models.requests.RideFeedbackRequest;
import com.gitlab.jspragadeesh.cabbookingapp.models.requests.RideIdRequest;
import com.gitlab.jspragadeesh.cabbookingapp.models.requests.RideRequest;
import com.gitlab.jspragadeesh.cabbookingapp.models.responses.RideResponse;
import com.gitlab.jspragadeesh.cabbookingapp.repositories.NodeRepository;
import com.gitlab.jspragadeesh.cabbookingapp.repositories.RideRepository;
import com.gitlab.jspragadeesh.cabbookingapp.repositories.UserRepository;
import com.gitlab.jspragadeesh.cabbookingapp.repositories.VehicleRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@CrossOrigin
@RequestMapping("api/rides")
@PreAuthorize("hasRole('USER')")
public class RideController {

    private final RideRepository rideRepository;
    private final UserRepository userRepository;
    private final NodeRepository nodeRepository;

    public RideController(RideRepository rideRepository, UserRepository userRepository,
                          NodeRepository nodeRepository) {
        this.rideRepository = rideRepository;
        this.userRepository = userRepository;
        this.nodeRepository = nodeRepository;
    }



    @PostMapping("/book")

    public @ResponseBody
    ResponseEntity<?> bookRide(Authentication authentication, @RequestBody RideRequest rideRequest){
        User user = userRepository.findByEmail(authentication.getName());

        // Check if the user has already booked a ride that is not completed or cancelled
        List<Ride> rides = rideRepository.findByCustomerAndStatusNotIn(user, Arrays.asList("COMPLETED", "CANCELLED"));
        if(rides.size() > 0){
            return ResponseEntity.badRequest().body("You have already booked a ride. Please cancel the ride before booking a new one.");
        }

        // Generating random coords for pickup and drop addresses for now (Without API)
        Coordinates pickUpCords = new Coordinates(rideRequest.getPickupAddress());
        Coordinates dropCords = new Coordinates(rideRequest.getDropAddress());
        Double distance = pickUpCords.getDistance(dropCords);
        Double estimatedRideFare = distance * rideRequest.getNoOfPassengers()
                * Vehicle.getFarePerKm(rideRequest.getPreferences().getVehicleType());

        // Get List of Nodes from DB
        List<Node> nodes = nodeRepository.findAll();

        // Get K nearest Nodes
        System.out.println("User Pickup " + pickUpCords);
        System.out.println("User Drop " + dropCords);
        List<Node> closestNodes = pickUpCords.findNearestNodes(nodes, 3);


        Ride ride = new Ride(
                user,
                pickUpCords,
                dropCords,
                distance,
                estimatedRideFare,
                rideRequest.getNoOfPassengers(),
                rideRequest.getPreferences()
        );
       // Add User to the nodes
        closestNodes.forEach(node -> node.addRide(ride));
        nodeRepository.saveAll(closestNodes);
        rideRepository.save(ride);
        return null;
    }




    // Start Ride
    @PutMapping("/start-ride")
    @PreAuthorize("hasRole('DRIVER')")
    public @ResponseBody
    ResponseEntity<?> startRide(Authentication authentication, @RequestBody RideIdRequest rideIDRequest){
        User driver = userRepository.findByEmail(authentication.getName());

        Optional<Ride> rideOptional = rideRepository.findById(rideIDRequest.getRideId());
        if(!rideOptional.isPresent()){
            return ResponseEntity.badRequest().body("Ride not found");
        }
        Ride ride = rideOptional.get();
        // Check if current driver has accepted the ride
        if(!ride.getDriver().getId().equals(driver.getId())){
            return ResponseEntity.badRequest().body("You are not the driver of this ride");
        }

        ride.setStatus("ONGOING");
        ride.setPickUpTime(new Date());
        rideRepository.save(ride);
        return ResponseEntity.ok("Ride started successfully");
    }

    // End Ride
    @PutMapping("/end-ride")
    @PreAuthorize("hasRole('DRIVER')")
    public @ResponseBody
    ResponseEntity<?> endRide(Authentication authentication, @RequestBody RideIdRequest rideIDRequest) {
        User driver = userRepository.findByEmail(authentication.getName());
        Optional<Ride> rideOptional = rideRepository.findById(rideIDRequest.getRideId());
        if(!rideOptional.isPresent()) {
            return ResponseEntity.badRequest().body("Ride not found");
        }
        Ride ride = rideOptional.get();
        // Check if current driver has accepted the ride
        if(!ride.getDriver().getId().equals(driver.getId())){
            return ResponseEntity.badRequest().body("You are not the driver of this ride");
        }

        Date dropTime = new Date();

        // Calculate RideFare based on ride duration
        double rideFare = ride.getEstimatedRideAmount() + (dropTime.getTime() - ride.getPickUpTime().getTime()) * 0.001;

        ride.setDropTime(dropTime);
        ride.setStatus("COMPLETED");
        ride.getRideAmount().setRideFare(rideFare);
        rideRepository.save(ride);


        // Add the rideFare to the driver's wallet
        driver.setWalletBalance(driver.getWalletBalance() + rideFare);

        RideResponse rideResponse = new RideResponse(ride.getId(), "COMPLETED", rideFare, new Date());
        return ResponseEntity.ok(rideResponse);
    }

    @PutMapping("/ride-feedback")
    @PreAuthorize("hasRole('USER')")
    public @ResponseBody
    ResponseEntity<?> rideFeedback(Authentication authentication, @RequestBody RideFeedbackRequest rideFeedbackRequest){
        User user = userRepository.findByEmail(authentication.getName());
        Optional<Ride> rideOptional = rideRepository.findById(rideFeedbackRequest.getRideId());
        if(!rideOptional.isPresent()){
            return ResponseEntity.badRequest().body("Ride not found");
        }
        Ride ride = rideOptional.get();
        // Check if current user has accepted the ride
        if(!ride.getCustomer().getId().equals(user.getId())){
            return ResponseEntity.badRequest().body("You did not book this ride");
        }

        // Check if Ride Status is completed
        if(!ride.getStatus().equals("COMPLETED")){
            return ResponseEntity.badRequest().body("Ride is not completed yet");
        }

        ride.setFeedback(rideFeedbackRequest.getFeedback());
        ride.setRating(rideFeedbackRequest.getRating());


        Amount amt = ride.getRideAmount();
        amt.setTip(rideFeedbackRequest.getTip());
        amt.setRideFare(amt.getRideFare());
        amt.setTotal(rideFeedbackRequest.getTip() + amt.getRideFare());

        ride.getRideAmount().setTip(rideFeedbackRequest.getTip());
        rideRepository.save(ride);

        // Update driver's rating
        User driver = ride.getDriver();
        double driverRating = driver.getRating();
        int driverRatingCount = driver.getRatingCount();
        driverRating = (driverRating * driverRatingCount + rideFeedbackRequest.getRating()) / (driverRatingCount + 1);
        driver.setRating(driverRating);
        driver.setRatingCount(driverRatingCount + 1);

        // Add tip to driver's wallet
        driver.setWalletBalance(driver.getWalletBalance() + rideFeedbackRequest.getTip());
        userRepository.save(driver);

        return ResponseEntity.ok("Ride feedback added successfully");
    }

}