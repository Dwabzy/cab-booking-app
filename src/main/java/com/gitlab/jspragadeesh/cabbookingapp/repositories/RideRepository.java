package com.gitlab.jspragadeesh.cabbookingapp.repositories;

import com.gitlab.jspragadeesh.cabbookingapp.models.Ride;
import com.gitlab.jspragadeesh.cabbookingapp.models.User;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RideRepository extends MongoRepository<Ride, String> {
    Optional<Ride> findByCustomerId(String customerId);
    Optional<Ride> findByDriverId(String driverId);
    List<Ride> findByCustomerAndStatusNotIn(User customer, List<String> status);
}
