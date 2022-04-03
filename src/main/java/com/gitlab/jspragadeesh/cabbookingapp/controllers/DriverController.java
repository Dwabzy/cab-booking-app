package com.gitlab.jspragadeesh.cabbookingapp.controllers;

import com.gitlab.jspragadeesh.cabbookingapp.models.DriverRidePreferences;
import com.gitlab.jspragadeesh.cabbookingapp.models.User;
import com.gitlab.jspragadeesh.cabbookingapp.repositories.UserRepository;
import com.gitlab.jspragadeesh.cabbookingapp.repositories.VehicleRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin
@RequestMapping("api/driver")
@PreAuthorize("hasRole('DRIVER')")
public class DriverController {

    private final UserRepository userRepository;
    private final VehicleRepository vehicleRepository;

    public DriverController(UserRepository userRepository, VehicleRepository vehicleRepository) {
        this.userRepository = userRepository;
        this.vehicleRepository = vehicleRepository;
    }

    @PutMapping("/preferences")
    public @ResponseBody
    ResponseEntity<?> updatePreferences(Authentication authentication, @RequestBody DriverRidePreferences preferences) {
        User currentUser = userRepository.findByEmail(authentication.getName());
        currentUser.setDriverRidePreferences(preferences);
        userRepository.save(currentUser);
        return ResponseEntity.ok("Preferences updated successfully");
    }
}
