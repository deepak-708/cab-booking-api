package com.cabapp.strategies.impl;

import com.cabapp.dto.RideRequestDto;
import com.cabapp.strategies.PricingStrategy;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SurgePricingStrategy implements PricingStrategy {
    private final DefaultPricingStrategy defaultPricingStrategy;
    private static final double SURGE_FACTOR = 1.5;

    @Override
    public double calculateFare(RideRequestDto request) {
        return defaultPricingStrategy.calculateFare(request) * SURGE_FACTOR;
    }
}