package com.cabapp.dto;

import com.cabapp.enums.TripStatus;
import lombok.Data;
import java.time.LocalDateTime;

@Data
public class TripDto {
    private Long id;
    private DriverDto driver; // Nested DTO
    private RiderDto rider;   // Nested DTO
    private Double fare;
    private Double distance;
    private TripStatus status;
    private LocalDateTime createdAt;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
}