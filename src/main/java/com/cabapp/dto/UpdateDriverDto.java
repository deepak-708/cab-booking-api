package com.cabapp.dto;

import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class UpdateDriverDto {
    private String name;

    @Pattern(regexp = "^\\d{10}$", message = "Phone must be 10 digits")
    private String phone;

    private String vehicleId;
}