package com.parcial2.trip.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import org.hibernate.annotations.CreationTimestamp;

@Entity
@Table(name = "trips")
public class Trip {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(nullable = false)
  private String passengerName;

  @Column(nullable = false)
  private String pickupAddress;

  @Column(nullable = false)
  private String destinationAddress;

  @Column(nullable = false)
  private String taxiPlate;

  @Column(nullable = false)
  private BigDecimal fare;

  @Enumerated(EnumType.STRING)
  @Column(nullable = false)
  private TripStatus status;

  @Column(nullable = false)
  private String requestedBy;

  @CreationTimestamp
  @Column(nullable = false, updatable = false)
  private LocalDateTime createdAt;

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getPassengerName() {
    return passengerName;
  }

  public void setPassengerName(String passengerName) {
    this.passengerName = passengerName;
  }

  public String getPickupAddress() {
    return pickupAddress;
  }

  public void setPickupAddress(String pickupAddress) {
    this.pickupAddress = pickupAddress;
  }

  public String getDestinationAddress() {
    return destinationAddress;
  }

  public void setDestinationAddress(String destinationAddress) {
    this.destinationAddress = destinationAddress;
  }

  public String getTaxiPlate() {
    return taxiPlate;
  }

  public void setTaxiPlate(String taxiPlate) {
    this.taxiPlate = taxiPlate;
  }

  public BigDecimal getFare() {
    return fare;
  }

  public void setFare(BigDecimal fare) {
    this.fare = fare;
  }

  public TripStatus getStatus() {
    return status;
  }

  public void setStatus(TripStatus status) {
    this.status = status;
  }

  public String getRequestedBy() {
    return requestedBy;
  }

  public void setRequestedBy(String requestedBy) {
    this.requestedBy = requestedBy;
  }

  public LocalDateTime getCreatedAt() {
    return createdAt;
  }
}

