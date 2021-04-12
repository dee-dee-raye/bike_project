package com.globomatics.bike.services;

import com.globomatics.bike.exceptions.BikeNotFoundException;
import com.globomatics.bike.models.Bike;
import com.globomatics.bike.repositories.BikeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class BikeService {

    @Autowired
    private BikeRepository bikeRepository;

    public List<Bike> list() {
        return bikeRepository.findAll();
    }

    public Bike get(Long id) throws BikeNotFoundException {
        Optional<Bike> optionalBike = bikeRepository.findById(id);
        if (optionalBike.isPresent()) {
            return optionalBike.get();
        } else {
            throw new BikeNotFoundException("Bike does not exist.");
        }
    }

    public void create(Bike bike) {
        bikeRepository.saveAndFlush(bike);
    }
}
