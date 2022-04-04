package com.gitlab.jspragadeesh.cabbookingapp.controllers;

import com.gitlab.jspragadeesh.cabbookingapp.models.User;
import com.gitlab.jspragadeesh.cabbookingapp.models.Vehicle;
import com.gitlab.jspragadeesh.cabbookingapp.models.responses.GenericResponse;
import com.gitlab.jspragadeesh.cabbookingapp.repositories.UserRepository;
import com.gitlab.jspragadeesh.cabbookingapp.repositories.VehicleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.web.bind.annotation.*;

import javax.annotation.security.RolesAllowed;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;


@RestController
@CrossOrigin
@RequestMapping("/api/vehicle")
public class VehicleController {
    private final VehicleRepository vehicleRepository;
    private final UserRepository userRepository;


    @Autowired
    public VehicleController(VehicleRepository vehicleRepository, UserRepository userRepository) {
        this.vehicleRepository = vehicleRepository;
        this.userRepository = userRepository;
    }

    @GetMapping("/all")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<GenericResponse<?>> getAllVehicles(Authentication authentication) {
        return ResponseEntity.ok().body(new GenericResponse<>(vehicleRepository.findAll()));
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMIN') or hasRole('DRIVER')")
    public ResponseEntity<GenericResponse<?>> getVehicleById(Authentication authentication,
                                                             @RequestBody Vehicle vehicle) {

        Optional<Vehicle> vehicleOptional = vehicleRepository.findById(vehicle.getId());

        if(!vehicleOptional.isPresent()){
            return ResponseEntity.badRequest().body(
                    new GenericResponse<>("Vehicle with id " + vehicle.getId() + " does not exist"));
        }
        Vehicle vehicleById = vehicleOptional.get();
        User user = userRepository.findByEmail(authentication.getName());
        // Check if driver is the owner of the vehicle or if user is an admin
        if(!(user.getId().equals(vehicleById.getDriverId())
                || authentication.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_ADMIN")))){
            return ResponseEntity.badRequest().body(
                    new GenericResponse<>("You are not authorized to view this vehicle"));
        }

        return ResponseEntity.ok().body(new GenericResponse<>(vehicleById));
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<GenericResponse<?>> createVehicle(@RequestBody Vehicle vehicle) {
        // Check if vehicle with same plateNumber exists
        if(vehicleRepository.existsByPlateNumber(vehicle.getPlateNumber())){
            return ResponseEntity.badRequest().body(
                    new GenericResponse<>("Vehicle with plate number " + vehicle.getPlateNumber() + " already exists"));
        }
        vehicle.setFarePerKm(Vehicle.getFarePerKm(vehicle.getType()));
        return ResponseEntity.ok().body(new GenericResponse<>(vehicleRepository.save(vehicle)));
    }

    @PutMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> updateVehicle(@RequestBody Vehicle vehicle) {
        if(!vehicleRepository.existsById(vehicle.getId())){
            return ResponseEntity.badRequest().body("Vehicle with id " + vehicle.getId() + " does not exist");
        }
        vehicleRepository.save(vehicle);
        return ResponseEntity.ok().body("Vehicle with id " + vehicle.getId() + " updated successfully");
    }

    @DeleteMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Vehicle> deleteVehicle(@RequestBody Vehicle vehicle) {
        vehicleRepository.delete(vehicle);
        return ResponseEntity.ok().body(vehicle);
    }
}
