package com.cabapp.dto;

import com.cabapp.enums.DriverStatus;
import lombok.Data;

@Data
public class DriverDto {
    private String name;
    private String email;
    private String phone;
    private String vehicleId;
    private Double rating;
    private DriverStatus status;
}