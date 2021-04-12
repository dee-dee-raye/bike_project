package com.globomatics.bike.controllers;

import com.globomatics.bike.models.AuthenticationRequest;
import com.globomatics.bike.models.AuthenticationResponse;
import com.globomatics.bike.models.Bike;
import com.globomatics.bike.models.BikeUserDetails;
import com.globomatics.bike.services.BikeUserDetailsService;
import com.globomatics.bike.utilities.JWTUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/api/v1")
@CrossOrigin(origins = "http://localhost:8081")
public class LoginController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private BikeUserDetailsService userDetailsService;

    @Autowired
    private JWTUtil jwtUtil;


    @PostMapping("/login")
    public AuthenticationResponse login(@RequestBody AuthenticationRequest request) {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword())
            );

        } catch (BadCredentialsException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Incorrect username or password");
        }

        BikeUserDetails userDetails = userDetailsService.loadUserByUsername(request.getUsername());
        System.out.println(userDetails.getNickname());
        String jwt = jwtUtil.generateToken(userDetails);
        return new AuthenticationResponse(jwt);
    }

    @GetMapping("/logout")
    public String restrictedHelloWorld() {
        return "Logout";
    }
}
