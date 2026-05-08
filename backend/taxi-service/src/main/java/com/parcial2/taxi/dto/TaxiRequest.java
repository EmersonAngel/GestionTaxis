package com.parcial2.taxi.dto;

import com.parcial2.taxi.model.TaxiStatus;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;

public record TaxiRequest(
    @NotBlank String plate,
    @NotBlank String driverName,
    @NotBlank String vehicleModel,
    @Min(1) Integer capacity,
    @NotNull @DecimalMin("0.0") BigDecimal baseFare,
    TaxiStatus status
) {
}

