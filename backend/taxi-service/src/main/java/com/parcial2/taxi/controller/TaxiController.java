package com.parcial2.taxi.controller;

import com.parcial2.taxi.dto.TaxiRequest;
import com.parcial2.taxi.dto.TaxiResponse;
import com.parcial2.taxi.service.TaxiService;
import jakarta.validation.Valid;
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
@RequestMapping("/api/taxis")
public class TaxiController {

  private final TaxiService taxiService;

  public TaxiController(TaxiService taxiService) {
    this.taxiService = taxiService;
  }

  @GetMapping
  public List<TaxiResponse> findAll(@RequestParam(required = false) String search) {
    return taxiService.findAll(search);
  }

  @GetMapping("/{id}")
  public TaxiResponse findById(@PathVariable Long id) {
    return taxiService.findById(id);
  }

  @PostMapping
  @ResponseStatus(HttpStatus.CREATED)
  @PreAuthorize("hasRole('ADMIN')")
  public TaxiResponse create(@Valid @RequestBody TaxiRequest request) {
    return taxiService.create(request);
  }

  @PutMapping("/{id}")
  @PreAuthorize("hasRole('ADMIN')")
  public TaxiResponse update(@PathVariable Long id, @Valid @RequestBody TaxiRequest request) {
    return taxiService.update(id, request);
  }

  @DeleteMapping("/{id}")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  @PreAuthorize("hasRole('ADMIN')")
  public void delete(@PathVariable Long id) {
    taxiService.delete(id);
  }
}

