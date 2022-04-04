package com.gitlab.jspragadeesh.cabbookingapp.controllers;

import com.gitlab.jspragadeesh.cabbookingapp.models.User;
import com.gitlab.jspragadeesh.cabbookingapp.models.requests.AddRoleRequest;
import com.gitlab.jspragadeesh.cabbookingapp.models.responses.GenericResponse;
import com.gitlab.jspragadeesh.cabbookingapp.repositories.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@CrossOrigin
@RequestMapping("api/admin")
@PreAuthorize("hasRole('ADMIN')")
public class AdminController {

    private final UserRepository userRepository;

    public AdminController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @PutMapping("/add-role")
    public @ResponseBody
    ResponseEntity<GenericResponse<?>> addRole(@RequestBody AddRoleRequest request) {
        if(request.getUserId() == null || request.getRole() == null) {
            return ResponseEntity.badRequest().body(new GenericResponse<>("Invalid request"));
        }

        Optional<User> userOptional = userRepository.findById(request.getUserId());
        if(!userOptional.isPresent()) {
            return ResponseEntity.badRequest().body(new GenericResponse<>("User not found"));
        }

        User user = userOptional.get();
        user.addRole(request.getRole());
        userRepository.save(user);
        return ResponseEntity.ok(new GenericResponse<>("Role added successfully", user.getRoles()));
    }
}