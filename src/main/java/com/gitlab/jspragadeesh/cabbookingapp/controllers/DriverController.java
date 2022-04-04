package com.gitlab.jspragadeesh.cabbookingapp.controllers;

import com.gitlab.jspragadeesh.cabbookingapp.models.*;
import com.gitlab.jspragadeesh.cabbookingapp.models.requests.RideIdRequest;
import com.gitlab.jspragadeesh.cabbookingapp.models.requests.RideRequest;
import com.gitlab.jspragadeesh.cabbookingapp.repositories.NodeRepository;
import com.gitlab.jspragadeesh.cabbookingapp.repositories.RideRepository;
import com.gitlab.jspragadeesh.cabbookingapp.repositories.UserRepository;
import com.gitlab.jspragadeesh.cabbookingapp.repositories.VehicleRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.stream.Collectors;

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
    ResponseEntity<?> acceptRide(Authentication authentication, @RequestBody RideIdRequest rideIdRequest) {
        User currentUser = userRepository.findByEmail(authentication.getName());
        Optional<Ride> rideOptional = rideRepository.findById(rideIdRequest.getRideId());
        if(!rideOptional.isPresent()) {
            return ResponseEntity.badRequest().body("Ride not found");
        }

        // Check if ride is already accepted
        if(rideOptional.get().getDriver() != null) {
            return ResponseEntity.badRequest().body("Ride already accepted");
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

    @PostMapping("/find-ride")
    public @ResponseBody
    ResponseEntity<?> findRide(Authentication authentication){

        User driver = userRepository.findByEmail(authentication.getName());
        Optional<Vehicle> vehicle = vehicleRepository.findByDriverId(driver.getId());
        if(!vehicle.isPresent()){
            return ResponseEntity.badRequest().body("You have not registered a vehicle");
        }

        // Get all rides from the k closest nodes to the driver
        List<Node> nodes = nodeRepository.findAll();
        Coordinates driverCords = new Coordinates(Math.random()*100, Math.random()*100);
        List<Node> closestNodes = driverCords.findNearestNodes(nodes, 3);

        System.out.println("Closest nodes to driver: " + closestNodes);

        // Find all rides from all the nodes that are closest to the driver
        List<Ride> rideList = closestNodes.stream().map(Node::getRides).flatMap(List::stream).collect(Collectors.toList());

        for(Ride ride : rideList){
            System.out.println(ride.getId());
        }

        // Remove duplicate Rides using Id (Due to adding ride to multiple nodes)
        Set<Ride> rideSet = new HashSet<>(rideList);
        List<Ride> rides = new ArrayList<>(rideSet);
        System.out.println("Total Rides nearby: " + rides.size());

        // TODO: Filter rides based on the preferences
        rides.removeIf(ride -> !driver.getDriverRidePreferences().doesRideSatisfyPreferences(ride));

        System.out.println("Filtered rides: " + rides);

        // Filter rides that are PENDING
        List<Ride> pendingRides = rides.stream().filter(ride -> ride.getStatus().equals("PENDING")).collect(Collectors.toList());

        return ResponseEntity.ok(pendingRides);
    }
}
