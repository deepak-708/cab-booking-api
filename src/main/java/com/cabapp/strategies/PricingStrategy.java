package com.cabapp.strategies;

import com.cabapp.dto.RideRequestDto;

public interface PricingStrategy {
    double calculateFare(RideRequestDto request);
}
