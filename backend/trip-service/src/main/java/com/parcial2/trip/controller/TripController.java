package com.parcial2.trip.controller;

import com.parcial2.trip.dto.TripRequest;
import com.parcial2.trip.dto.TripResponse;
import com.parcial2.trip.service.TripService;
import jakarta.validation.Valid;
import java.security.Principal;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/trips")
public class TripController {

  private final TripService tripService;

  public TripController(TripService tripService) {
    this.tripService = tripService;
  }

  @GetMapping
  public List<TripResponse> findAll(@RequestParam(required = false) String search) {
    return tripService.findAll(search);
  }

  @GetMapping("/{id}")
  public TripResponse findById(@PathVariable Long id) {
    return tripService.findById(id);
  }

  @PostMapping
  @ResponseStatus(HttpStatus.CREATED)
  @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
  public TripResponse create(@Valid @RequestBody TripRequest request, Principal principal) {
    return tripService.create(request, principal);
  }

  @PutMapping("/{id}")
  @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
  public TripResponse update(@PathVariable Long id, @Valid @RequestBody TripRequest request) {
    return tripService.update(id, request);
  }

  @DeleteMapping("/{id}")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  @PreAuthorize("hasRole('ADMIN')")
  public void delete(@PathVariable Long id) {
    tripService.delete(id);
  }
}

