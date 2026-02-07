package com.cabapp.strategies;

import com.cabapp.entity.Driver;
import com.cabapp.entity.Location;
import com.cabapp.strategies.impl.NearestDriverMatchingStrategy;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class DriverMatchingStrategyManager {

    private final NearestDriverMatchingStrategy nearestDriverMatchingStrategy;
    // private final HighestRatedDriverMatchingStrategy highestRatedStrategy; // Future injection

    public List<Driver> findMatchingDrivers(Location pickupLocation) {
        // FUTURE LOGIC:
        // if (isPremiumUser) return highestRatedStrategy.findMatchingDrivers(pickupLocation);

        // Default Strategy
        return nearestDriverMatchingStrategy.findMatchingDrivers(pickupLocation);
    }
}