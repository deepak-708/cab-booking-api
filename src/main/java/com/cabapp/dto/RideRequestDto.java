package com.cabapp.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class RideRequestDto {
    @NotNull(message = "Pickup Latitude is required")
    private Double pickupLat;

    @NotNull(message = "Pickup Longitude is required")
    private Double pickupLng;

    @NotNull(message = "Drop Latitude is required")
    private Double dropLat;

    @NotNull(message = "Drop Longitude is required")
    private Double dropLng;

    // Optional: Only used if admin/rider passes it explicitly,
    // otherwise derived from token
    private String riderEmail;
}