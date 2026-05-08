package com.parcial2.taxi.service;

import com.parcial2.taxi.dto.TaxiRequest;
import com.parcial2.taxi.dto.TaxiResponse;
import com.parcial2.taxi.model.Taxi;
import com.parcial2.taxi.model.TaxiStatus;
import com.parcial2.taxi.repository.TaxiRepository;
import jakarta.persistence.EntityNotFoundException;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class TaxiService {

  private final TaxiRepository taxiRepository;

  public TaxiService(TaxiRepository taxiRepository) {
    this.taxiRepository = taxiRepository;
  }

  public List<TaxiResponse> findAll(String search) {
    List<Taxi> taxis = search == null || search.isBlank()
        ? taxiRepository.findAll()
        : taxiRepository.findByPlateContainingIgnoreCaseOrDriverNameContainingIgnoreCase(search, search);
    return taxis.stream().map(this::toResponse).toList();
  }

  public TaxiResponse findById(Long id) {
    return taxiRepository.findById(id)
        .map(this::toResponse)
        .orElseThrow(() -> new EntityNotFoundException("Taxi no encontrado"));
  }

  @Transactional
  public TaxiResponse create(TaxiRequest request) {
    if (taxiRepository.existsByPlateIgnoreCase(request.plate())) {
      throw new IllegalArgumentException("Ya existe un taxi con esa placa");
    }
    Taxi taxi = new Taxi();
    apply(request, taxi);
    return toResponse(taxiRepository.save(taxi));
  }

  @Transactional
  public TaxiResponse update(Long id, TaxiRequest request) {
    Taxi taxi = taxiRepository.findById(id)
        .orElseThrow(() -> new EntityNotFoundException("Taxi no encontrado"));
    apply(request, taxi);
    return toResponse(taxiRepository.save(taxi));
  }

  public void delete(Long id) {
    if (!taxiRepository.existsById(id)) {
      throw new EntityNotFoundException("Taxi no encontrado");
    }
    taxiRepository.deleteById(id);
  }

  private void apply(TaxiRequest request, Taxi taxi) {
    taxi.setPlate(request.plate().toUpperCase());
    taxi.setDriverName(request.driverName());
    taxi.setVehicleModel(request.vehicleModel());
    taxi.setCapacity(request.capacity());
    taxi.setBaseFare(request.baseFare());
    taxi.setStatus(request.status() == null ? TaxiStatus.AVAILABLE : request.status());
  }

  private TaxiResponse toResponse(Taxi taxi) {
    return new TaxiResponse(
        taxi.getId(),
        taxi.getPlate(),
        taxi.getDriverName(),
        taxi.getVehicleModel(),
        taxi.getCapacity(),
        taxi.getBaseFare(),
        taxi.getStatus()
    );
  }
}

