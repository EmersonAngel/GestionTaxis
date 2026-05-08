package com.parcial2.trip.dto;

import com.parcial2.trip.model.TripStatus;
import java.math.BigDecimal;

public record TripResponse(
    Long id,
    String passengerName,
    String pickupAddress,
    String destinationAddress,
    String taxiPlate,
    BigDecimal fare,
    TripStatus status,
    String requestedBy
) {
}

