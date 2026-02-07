package com.cabapp.service;

import com.cabapp.dto.*;
import com.cabapp.entity.Driver;
import com.cabapp.enums.DriverStatus;
import com.cabapp.enums.Role;
import com.cabapp.exception.ResourceNotFoundException;
import com.cabapp.exception.UserAlreadyExistsException;
import com.cabapp.repository.DriverRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;

@Service
@RequiredArgsConstructor
public class DriverService {

    private final DriverRepository driverRepository;
    private final ModelMapper modelMapper;
    private final PasswordEncoder passwordEncoder;

    /**
     * Register a new Driver
     */
    @Transactional
    public DriverDto registerDriver(DriverSignupRequestDto request) {
        if (driverRepository.existsByEmail(request.getEmail())) {
            throw new UserAlreadyExistsException("Driver already exists with email: " + request.getEmail());
        }

        Driver driver = modelMapper.map(request, Driver.class);

        // Set Defaults
        driver.setPassword(passwordEncoder.encode(request.getPassword()));
        driver.setRoles(Set.of(Role.DRIVER));
        driver.setStatus(DriverStatus.OFFLINE); // Default to Offline
        driver.setRating(5.0);

        Driver savedDriver = driverRepository.save(driver);
        return modelMapper.map(savedDriver, DriverDto.class);
    }

    /**
     * Get Current Driver Profile
     */
    public DriverDto getMyProfile(String email) {
        Driver driver = getDriverByEmail(email);
        return modelMapper.map(driver, DriverDto.class);
    }

    /**
     * Update Driver Profile (Name, Phone, Vehicle)
     */
    @Transactional
    public DriverDto updateMyProfile(String email, UpdateDriverDto updateDto) {
        Driver driver = getDriverByEmail(email);

        if (updateDto.getName() != null) driver.setName(updateDto.getName());
        if (updateDto.getPhone() != null) driver.setPhone(updateDto.getPhone());
        if (updateDto.getVehicleId() != null) driver.setVehicleId(updateDto.getVehicleId());

        return modelMapper.map(driverRepository.save(driver), DriverDto.class);
    }

    /**
     * Delete Driver Account
     */
    @Transactional
    public void deleteMyAccount(String email) {
        Driver driver = getDriverByEmail(email);
        driverRepository.delete(driver);
    }

    /**
     * Update Status (ONLINE / OFFLINE)
     */
    @Transactional
    public DriverDto updateStatus(String email, UpdateDriverStatusDto statusDto) {
        Driver driver = getDriverByEmail(email);

        // Validation: Cannot go OFFLINE if currently ON_TRIP
        if (driver.getStatus() == DriverStatus.ON_TRIP && statusDto.getStatus() == DriverStatus.OFFLINE) {
            throw new IllegalStateException("Cannot go OFFLINE while on a trip.");
        }

        driver.setStatus(statusDto.getStatus());
        return modelMapper.map(driverRepository.save(driver), DriverDto.class);
    }

    /**
     * Update Location (Lat/Lng)
     * This is called frequently (e.g., every 10s)
     */
    @Transactional
    public DriverDto updateLocation(String email, UpdateDriverLocationDto locationDto) {
        Driver driver = getDriverByEmail(email);

        driver.setLatitude(locationDto.getLatitude());
        driver.setLongitude(locationDto.getLongitude());

        return modelMapper.map(driverRepository.save(driver), DriverDto.class);
    }

    // Helper
    private Driver getDriverByEmail(String email) {
        return driverRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("Driver not found with email: " + email));
    }
}
