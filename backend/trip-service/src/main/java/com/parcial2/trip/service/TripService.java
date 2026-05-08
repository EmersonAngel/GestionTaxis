package com.parcial2.trip.service;

import com.parcial2.trip.dto.TripRequest;
import com.parcial2.trip.dto.TripResponse;
import com.parcial2.trip.model.Trip;
import com.parcial2.trip.model.TripStatus;
import com.parcial2.trip.repository.TripRepository;
import jakarta.persistence.EntityNotFoundException;
import java.security.Principal;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class TripService {

  private final TripRepository tripRepository;

  public TripService(TripRepository tripRepository) {
    this.tripRepository = tripRepository;
  }

  public List<TripResponse> findAll(String search) {
    List<Trip> trips = search == null || search.isBlank()
        ? tripRepository.findAll()
        : tripRepository.findByPassengerNameContainingIgnoreCaseOrTaxiPlateContainingIgnoreCase(search, search);
    return trips.stream().map(this::toResponse).toList();
  }

  public TripResponse findById(Long id) {
    return tripRepository.findById(id)
        .map(this::toResponse)
        .orElseThrow(() -> new EntityNotFoundException("Carrera no encontrada"));
  }

  @Transactional
  public TripResponse create(TripRequest request, Principal principal) {
    Trip trip = new Trip();
    apply(request, trip);
    trip.setRequestedBy(principal.getName());
    return toResponse(tripRepository.save(trip));
  }

  @Transactional
  public TripResponse update(Long id, TripRequest request) {
    Trip trip = tripRepository.findById(id)
        .orElseThrow(() -> new EntityNotFoundException("Carrera no encontrada"));
    apply(request, trip);
    return toResponse(tripRepository.save(trip));
  }

  public void delete(Long id) {
    if (!tripRepository.existsById(id)) {
      throw new EntityNotFoundException("Carrera no encontrada");
    }
    tripRepository.deleteById(id);
  }

  private void apply(TripRequest request, Trip trip) {
    trip.setPassengerName(request.passengerName());
    trip.setPickupAddress(request.pickupAddress());
    trip.setDestinationAddress(request.destinationAddress());
    trip.setTaxiPlate(request.taxiPlate().toUpperCase());
    trip.setFare(request.fare());
    trip.setStatus(request.status() == null ? TripStatus.REQUESTED : request.status());
  }

  private TripResponse toResponse(Trip trip) {
    return new TripResponse(
        trip.getId(),
        trip.getPassengerName(),
        trip.getPickupAddress(),
        trip.getDestinationAddress(),
        trip.getTaxiPlate(),
        trip.getFare(),
        trip.getStatus(),
        trip.getRequestedBy()
    );
  }
}

