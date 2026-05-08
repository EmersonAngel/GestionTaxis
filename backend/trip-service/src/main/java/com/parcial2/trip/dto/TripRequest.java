package com.parcial2.trip.dto;

import com.parcial2.trip.model.TripStatus;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;

public record TripRequest(
    @NotBlank String passengerName,
    @NotBlank String pickupAddress,
    @NotBlank String destinationAddress,
    @NotBlank String taxiPlate,
    @NotNull @DecimalMin("0.0") BigDecimal fare,
    TripStatus status
) {
}

