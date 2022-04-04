package com.gitlab.jspragadeesh.cabbookingapp.controllers;

import com.gitlab.jspragadeesh.cabbookingapp.models.CustomerRidePreferences;
import com.gitlab.jspragadeesh.cabbookingapp.models.User;
import com.gitlab.jspragadeesh.cabbookingapp.models.requests.UserRequest;
import com.gitlab.jspragadeesh.cabbookingapp.models.responses.GenericResponse;
import com.gitlab.jspragadeesh.cabbookingapp.models.responses.UserResponse;
import com.gitlab.jspragadeesh.cabbookingapp.repositories.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.HashSet;
import java.util.Set;


@RestController
@CrossOrigin
@RequestMapping("api/user")
//Check for role annotation
public class UserController {

    private final UserRepository userRepository;

    public UserController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    // Update User
    @PutMapping("/profile")
    @PreAuthorize("hasRole('USER')")
    public @ResponseBody
    ResponseEntity<GenericResponse<?>> updateUser(@RequestBody UserRequest userRequest, Authentication authentication) {
        User user = userRepository.findByEmail(authentication.getName());
        user.setFirstName(userRequest.getFirstName());
        user.setLastName(userRequest.getLastName());
        user.setGender(userRequest.getGender());
        user.setPhoneNumbers(userRequest.getPhoneNumbers());
        user.setEmail(userRequest.getEmail());
        userRepository.save(user);
        return ResponseEntity.ok(
                new GenericResponse<>("User updated successfully", new UserResponse(user)));
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
