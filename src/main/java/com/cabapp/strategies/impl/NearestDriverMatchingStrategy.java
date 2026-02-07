package com.cabapp.strategies.impl;

import com.cabapp.entity.Driver;
import com.cabapp.entity.Location;
import com.cabapp.repository.DriverRepository;
import com.cabapp.strategies.DriverMatchingStrategy;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
@RequiredArgsConstructor
public class NearestDriverMatchingStrategy implements DriverMatchingStrategy {
    private final DriverRepository driverRepository;

    @Override
    public List<Driver> findMatchingDrivers(Location pickupLocation) {
        return driverRepository.findTenNearestDrivers(
                pickupLocation.getLatitude(),
                pickupLocation.getLongitude(),
                5.0 // 5km Radius
        );
    }
}