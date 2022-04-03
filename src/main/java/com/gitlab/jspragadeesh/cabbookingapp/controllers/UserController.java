package com.gitlab.jspragadeesh.cabbookingapp.controllers;

import com.gitlab.jspragadeesh.cabbookingapp.models.CustomerRidePreferences;
import com.gitlab.jspragadeesh.cabbookingapp.models.RidePreferences;
import com.gitlab.jspragadeesh.cabbookingapp.models.User;
import com.gitlab.jspragadeesh.cabbookingapp.repositories.UserRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;


@RestController
@CrossOrigin
@RequestMapping("api/user")
//Check for role annotation
public class UserController {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserController(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @PostMapping("/register")
    public @ResponseBody
    ResponseEntity<String> registerUser(@RequestBody User user) {
        if(userRepository.findByEmail(user.getEmail()) != null) {
            return ResponseEntity.badRequest().body("User already exists");
        }
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.save(user);
        return ResponseEntity.ok("Registered successfully: " + user);
    }

    @GetMapping("/profile")
    @PreAuthorize("hasRole('USER')")
    public @ResponseBody
    ResponseEntity<?> getUserByEmail(Authentication authentication) {
        return ResponseEntity.ok(userRepository.findByEmail(authentication.getName()));
    }

    // Update Preferences
    @PutMapping("/preferences")
    @PreAuthorize("hasRole('USER')")
    public @ResponseBody
    ResponseEntity<?> updatePreferences(Authentication authentication, @RequestBody CustomerRidePreferences preferences) {
        User currentUser = userRepository.findByEmail(authentication.getName());
        currentUser.setCustomerRidePreferences(preferences);
        userRepository.save(currentUser);
        return ResponseEntity.ok("Preferences updated successfully");
    }
}
