package com.parcial2.trip.repository;

import com.parcial2.trip.model.Trip;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TripRepository extends JpaRepository<Trip, Long> {
  List<Trip> findByPassengerNameContainingIgnoreCaseOrTaxiPlateContainingIgnoreCase(String passengerName, String taxiPlate);
}

