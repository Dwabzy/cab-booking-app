package com.gitlab.jspragadeesh.cabbookingapp.controllers;

import com.gitlab.jspragadeesh.cabbookingapp.models.Vehicle;
import com.gitlab.jspragadeesh.cabbookingapp.repositories.VehicleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.NoSuchElementException;


@RestController
@CrossOrigin
@RequestMapping("/api/vehicle")
public class VehicleController {
    private VehicleRepository vehicleRepository;


    @Autowired
    public VehicleController(VehicleRepository vehicleRepository) {
        this.vehicleRepository = vehicleRepository;
    }

    @GetMapping("/all")
    public ResponseEntity<List<Vehicle>> getAllVehicles() {
        return ResponseEntity.ok().body(vehicleRepository.findAll());
    }

    @GetMapping
    public ResponseEntity<?> getVehicleById(@RequestBody Vehicle vehicle) {
        try{
            return ResponseEntity.ok().body(vehicleRepository.findById(vehicle.getId()).get());
        }catch (NoSuchElementException e){
            return ResponseEntity.badRequest().body("Vehicle with id " + vehicle.getId() + " does not exist");
        }

    }

    @PostMapping
    public ResponseEntity<Vehicle> createVehicle(@RequestBody Vehicle vehicle) {
        // Check if vehicle with same plateNumber exists
        if(vehicleRepository.existsByPlateNumber(vehicle.getPlateNumber())){
            return ResponseEntity.badRequest().body(vehicle);
        }
        return ResponseEntity.ok().body(vehicleRepository.save(vehicle));
    }

    @PutMapping
    public ResponseEntity<String> updateVehicle(@RequestBody Vehicle vehicle) {
        if(!vehicleRepository.existsById(vehicle.getId())){
            return ResponseEntity.badRequest().body("Vehicle with id " + vehicle.getId() + " does not exist");
        }
        vehicleRepository.save(vehicle);
        return ResponseEntity.ok().body("Vehicle with id " + vehicle.getId() + " updated successfully");
    }

    @DeleteMapping
    public ResponseEntity<Vehicle> deleteVehicle(@RequestBody Vehicle vehicle) {
        vehicleRepository.delete(vehicle);
        return ResponseEntity.ok().body(vehicle);
    }
}
