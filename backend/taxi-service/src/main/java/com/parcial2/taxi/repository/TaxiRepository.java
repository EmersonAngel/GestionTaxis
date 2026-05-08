package com.parcial2.taxi.repository;

import com.parcial2.taxi.model.Taxi;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TaxiRepository extends JpaRepository<Taxi, Long> {
  boolean existsByPlateIgnoreCase(String plate);

  List<Taxi> findByPlateContainingIgnoreCaseOrDriverNameContainingIgnoreCase(String plate, String driverName);
}

