package com.gitlab.jspragadeesh.cabbookingapp.repositories;

import com.gitlab.jspragadeesh.cabbookingapp.models.Ride;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface RideRepository extends MongoRepository<Ride, String> {
    Optional<Ride> findByCustomerId(String customerId);
    Optional<Ride> findByDriverId(String driverId);
}
