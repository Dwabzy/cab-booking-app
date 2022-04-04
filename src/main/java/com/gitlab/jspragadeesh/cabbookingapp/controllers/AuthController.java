package com.gitlab.jspragadeesh.cabbookingapp.controllers;

import com.gitlab.jspragadeesh.cabbookingapp.models.User;
import com.gitlab.jspragadeesh.cabbookingapp.models.requests.JwtRequest;
import com.gitlab.jspragadeesh.cabbookingapp.models.requests.UserRequest;
import com.gitlab.jspragadeesh.cabbookingapp.models.responses.GenericResponse;
import com.gitlab.jspragadeesh.cabbookingapp.models.responses.JwtResponse;
import com.gitlab.jspragadeesh.cabbookingapp.models.responses.UserResponse;
import com.gitlab.jspragadeesh.cabbookingapp.repositories.UserRepository;
import com.gitlab.jspragadeesh.cabbookingapp.services.UserService;
import com.gitlab.jspragadeesh.cabbookingapp.utility.JwtTokenUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.HashSet;
import java.util.Set;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    private final AuthenticationManager authenticationManager;
    private final JwtTokenUtil jwtTokenUtil;
    private final UserService userService;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public AuthController(AuthenticationManager authenticationManager, JwtTokenUtil jwtTokenUtil, UserService userService, UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.authenticationManager = authenticationManager;
        this.jwtTokenUtil = jwtTokenUtil;
        this.userService = userService;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @PostMapping("/register")
    public @ResponseBody
    ResponseEntity<GenericResponse<?>> registerUser(@RequestBody UserRequest userRequest) {
        if(userRepository.findByEmail(userRequest.getEmail()) != null) {
            return ResponseEntity.badRequest().body(new GenericResponse<>("User already exists"));
        }
        User user = new User(
                userRequest.getFirstName(),
                userRequest.getLastName(),
                userRequest.getGender(),
                userRequest.getPhoneNumbers(),
                userRequest.getEmail(),
                passwordEncoder.encode(userRequest.getPassword())
        );
        user.setRoles(new HashSet<>(Set.of("USER")));
        userRepository.save(user);
        return ResponseEntity.ok(
                new GenericResponse<>("User created successfully", new UserResponse(user)));
    }

    @PostMapping(path ="/login")
    public @ResponseBody ResponseEntity<GenericResponse<?>> createAuthenticationToken(@RequestBody JwtRequest authenticationRequest){
        try {
            authenticate(authenticationRequest.getEmail(), authenticationRequest.getPassword());
            final UserDetails userDetails = userService
                    .loadUserByUsername(authenticationRequest.getEmail());
            final String token = jwtTokenUtil.generateToken(userDetails);
            JwtResponse jwtResponse = new JwtResponse(token);
            return ResponseEntity.ok(new GenericResponse<>("User logged in successfully", jwtResponse));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new GenericResponse<>("Invalid username or password"));
        }

    }

    private void authenticate(String username, String password) throws Exception {
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
        } catch (DisabledException e) {
            throw new Exception("USER_DISABLED", e);
        } catch (BadCredentialsException e) {
            throw new Exception("INVALID_CREDENTIALS", e);
        }
    }
}
