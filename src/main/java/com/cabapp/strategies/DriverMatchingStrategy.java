package com.cabapp.strategies;

import com.cabapp.entity.Driver;
import com.cabapp.entity.Location;
import java.util.List;

public interface DriverMatchingStrategy {
    List<Driver> findMatchingDrivers(Location pickupLocation);
}