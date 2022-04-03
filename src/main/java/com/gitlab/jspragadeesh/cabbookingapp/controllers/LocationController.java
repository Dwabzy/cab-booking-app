package com.gitlab.jspragadeesh.cabbookingapp.controllers;

import com.gitlab.jspragadeesh.cabbookingapp.models.Location;
import com.gitlab.jspragadeesh.cabbookingapp.models.User;
import com.gitlab.jspragadeesh.cabbookingapp.repositories.UserRepository;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;




@RestController
@CrossOrigin
@RequestMapping("api/user/location")
public class LocationController {

    private UserRepository userRepository;

    @Autowired
    public LocationController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }


    @NoArgsConstructor
    @AllArgsConstructor
    @Data
    public static class LocationRequest {
        List<Location> locations;
    }

    @GetMapping
    public @ResponseBody
    ResponseEntity<List<Location>> getLocations(Authentication authentication) {
        User user = userRepository.findByEmail(authentication.getName());
        if(user == null) {
            return ResponseEntity.notFound().build();
        }
        if(user.getLocations() == null) {
            return ResponseEntity.ok(List.of());
        }
        return ResponseEntity.ok(user.getLocations());
    }

    @PutMapping
    public @ResponseBody
    ResponseEntity<String> updateLocation(Authentication authentication, @RequestBody LocationRequest locationBody) {
        User user = userRepository.findByEmail(authentication.getName());
        List<Location> locations = locationBody.getLocations();
        if(user == null) {
            return ResponseEntity.notFound().build();
        }

        for(Location location : locations) {
            Location locationFromDB = user.getLocationByName(location.getName());
            if(locationFromDB == null) {
                user.addLocation(location);
            } else {
                locationFromDB.setAddress(location.getAddress());
            }
        }
        userRepository.save(user);
        return ResponseEntity.ok("Location updated");
    }

    @DeleteMapping
    public @ResponseBody
    ResponseEntity<String> deleteLocation(Authentication authentication, @RequestBody Location location) {
        User user = userRepository.findByEmail(authentication.getName());
        if(user == null) {
            return ResponseEntity.notFound().build();
        }
        // If location exists, remove it
        if(user.getLocations().contains(location)) {
            user.removeLocation(location);
        }
        userRepository.save(user);
        return ResponseEntity.ok("Location removed");
    }

}
