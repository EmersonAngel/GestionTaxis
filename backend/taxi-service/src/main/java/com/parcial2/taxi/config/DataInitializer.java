package com.parcial2.taxi.config;

import com.parcial2.taxi.model.Taxi;
import com.parcial2.taxi.model.TaxiStatus;
import com.parcial2.taxi.repository.TaxiRepository;
import java.math.BigDecimal;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DataInitializer {

  @Bean
  CommandLineRunner seedTaxis(TaxiRepository taxiRepository) {
    return args -> {
      if (taxiRepository.count() == 0) {
        create(taxiRepository, "TAX-101", "Carlos Rivera", "Kia Picanto 2023", 4, "6200", TaxiStatus.AVAILABLE);
        create(taxiRepository, "TAX-204", "Laura Mendez", "Hyundai Accent 2022", 4, "6500", TaxiStatus.BUSY);
        create(taxiRepository, "TAX-317", "Miguel Torres", "Chevrolet Onix 2024", 4, "7000", TaxiStatus.MAINTENANCE);
      }
    };
  }

  private void create(
      TaxiRepository taxiRepository,
      String plate,
      String driver,
      String model,
      int capacity,
      String fare,
      TaxiStatus status
  ) {
    Taxi taxi = new Taxi();
    taxi.setPlate(plate);
    taxi.setDriverName(driver);
    taxi.setVehicleModel(model);
    taxi.setCapacity(capacity);
    taxi.setBaseFare(new BigDecimal(fare));
    taxi.setStatus(status);
    taxiRepository.save(taxi);
  }
}

