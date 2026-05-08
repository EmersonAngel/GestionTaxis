package com.parcial2.taxi.dto;

import com.parcial2.taxi.model.TaxiStatus;
import java.math.BigDecimal;

public record TaxiResponse(
    Long id,
    String plate,
    String driverName,
    String vehicleModel,
    Integer capacity,
    BigDecimal baseFare,
    TaxiStatus status
) {
}

