package com.parcial2.trip.config;

import com.parcial2.trip.model.Trip;
import com.parcial2.trip.model.TripStatus;
import com.parcial2.trip.repository.TripRepository;
import java.math.BigDecimal;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DataInitializer {

  @Bean
  CommandLineRunner seedTrips(TripRepository tripRepository) {
    return args -> {
      if (tripRepository.count() == 0) {
        create(tripRepository, "Ana Perez", "Terminal de Transporte", "Centro Comercial Unico", "TAX-101", "18500", TripStatus.COMPLETED);
        create(tripRepository, "Jose Gomez", "Universidad", "Barrio Prado", "TAX-204", "14200", TripStatus.ASSIGNED);
      }
    };
  }

  private void create(
      TripRepository tripRepository,
      String passenger,
      String pickup,
      String destination,
      String plate,
      String fare,
      TripStatus status
  ) {
    Trip trip = new Trip();
    trip.setPassengerName(passenger);
    trip.setPickupAddress(pickup);
    trip.setDestinationAddress(destination);
    trip.setTaxiPlate(plate);
    trip.setFare(new BigDecimal(fare));
    trip.setStatus(status);
    trip.setRequestedBy("admin@taxis.com");
    tripRepository.save(trip);
  }
}

