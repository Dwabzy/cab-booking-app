package com.gitlab.jspragadeesh.cabbookingapp.repositories;

import com.gitlab.jspragadeesh.cabbookingapp.models.Vehicle;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface VehicleRepository extends MongoRepository<Vehicle, String> {
    boolean existsByPlateNumber(String plateNumber);
    Optional<Vehicle> findByDriverId(String driverId);
}
