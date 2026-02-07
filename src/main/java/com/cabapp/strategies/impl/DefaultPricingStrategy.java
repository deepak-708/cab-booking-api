package com.cabapp.strategies.impl;

import com.cabapp.dto.RideRequestDto;
import com.cabapp.strategies.PricingStrategy;
import org.springframework.stereotype.Service;

@Service
public class DefaultPricingStrategy implements PricingStrategy {
    private static final double PER_KM_RATE = 10.0;

    @Override
    public double calculateFare(RideRequestDto request) {
        double distance = calculateDistance(request.getPickupLat(), request.getPickupLng(), request.getDropLat(), request.getDropLng());
        return Math.max(distance * PER_KM_RATE, 50.0); // Minimum fare 50
    }

    private double calculateDistance(Double lat1, Double lon1, Double lat2, Double lon2) {
        // Haversine
        final int R = 6371;
        double latDistance = Math.toRadians(lat2 - lat1);
        double lonDistance = Math.toRadians(lon2 - lon1);
        double a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2)
                + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2))
                * Math.sin(lonDistance / 2) * Math.sin(lonDistance / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        return R * c;
    }
}
