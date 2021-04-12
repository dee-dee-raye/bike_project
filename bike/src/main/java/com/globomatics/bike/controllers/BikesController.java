package com.globomatics.bike.controllers;

import com.globomatics.bike.exceptions.BikeNotFoundException;
import com.globomatics.bike.models.Bike;
import com.globomatics.bike.services.BikeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/api/v1/bikes")
@CrossOrigin(origins = "http://localhost:8081")
public class BikesController {

    @Autowired
    private BikeService bikeService;

    @GetMapping
    public List<Bike> list() {
        return bikeService.list();
    }

    @PostMapping
    @ResponseStatus(HttpStatus.OK)
    public void create(@RequestBody Bike bike) {
        bikeService.create(bike);
    }

    @GetMapping("/{id}")
    public Bike get(@PathVariable Long id) {
        try {
            return bikeService.get(id);
        } catch (BikeNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Bike not found.");
        }
    }
}
