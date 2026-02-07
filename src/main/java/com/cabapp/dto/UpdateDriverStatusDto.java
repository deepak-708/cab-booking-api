package com.cabapp.dto;

import com.cabapp.enums.DriverStatus;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class UpdateDriverStatusDto {
    @NotNull(message = "Status is required")
    private DriverStatus status;
}