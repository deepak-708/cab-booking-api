package com.cabapp.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class UpdateDriverLocationDto {
    @NotNull(message = "Latitude is required")
    private Double latitude;

    @NotNull(message = "Longitude is required")
    private Double longitude;
}