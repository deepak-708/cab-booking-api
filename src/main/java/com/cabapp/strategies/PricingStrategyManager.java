package com.cabapp.strategies;

import com.cabapp.dto.RideRequestDto;
import com.cabapp.strategies.impl.DefaultPricingStrategy;
import com.cabapp.strategies.impl.SurgePricingStrategy;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import java.time.LocalTime;

@Component
@RequiredArgsConstructor
public class PricingStrategyManager {
    private final DefaultPricingStrategy defaultPricingStrategy;
    private final SurgePricingStrategy surgePricingStrategy;

    public double calculateFare(RideRequestDto request) {
        LocalTime now = LocalTime.now();
        // Surge: 8-11 AM OR 6-9 PM
        boolean isSurge = (now.isAfter(LocalTime.of(8, 0)) && now.isBefore(LocalTime.of(11, 0))) ||
                (now.isAfter(LocalTime.of(18, 0)) && now.isBefore(LocalTime.of(21, 0)));

        return isSurge ? surgePricingStrategy.calculateFare(request) : defaultPricingStrategy.calculateFare(request);
    }
}