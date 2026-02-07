package com.cabapp.service;

import com.cabapp.dto.RideRequestDto;
import com.cabapp.dto.TripDto;
import com.cabapp.entity.*;
import com.cabapp.enums.DriverStatus;
import com.cabapp.enums.TripStatus;
import com.cabapp.exception.ResourceNotFoundException;
import com.cabapp.repository.DriverRepository;
import com.cabapp.repository.RiderRepository;
import com.cabapp.repository.TripRepository;
import com.cabapp.strategies.DriverMatchingStrategyManager;
import com.cabapp.strategies.PricingStrategyManager;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TripService {

    private final TripRepository tripRepository;
    private final RiderRepository riderRepository;
    private final DriverRepository driverRepository;
    private final DriverMatchingStrategyManager driverMatchingStrategyManager;
    private final PricingStrategyManager pricingStrategyManager;
    private final ModelMapper modelMapper;

    /**
     * 1. Public Estimate API
     */
    public Double calculateEstimate(RideRequestDto request) {
        return pricingStrategyManager.calculateFare(request);
    }

    /**
     * 2. Book Ride API
     */
    @Transactional
    public TripDto requestTrip(RideRequestDto request, String riderEmail) {
        Rider rider = riderRepository.findByEmail(riderEmail)
                .orElseThrow(() -> new ResourceNotFoundException("Rider not found: " + riderEmail));

        // Strategy 1: Find Driver
        Location pickup = new Location(request.getPickupLat(), request.getPickupLng());
        List<Driver> availableDrivers = driverMatchingStrategyManager.findMatchingDrivers(pickup);

        if (availableDrivers.isEmpty()) {
            throw new RuntimeException("No drivers available nearby.");
        }
        Driver matchedDriver = availableDrivers.get(0); // Pick first available

        // Strategy 2: Calculate Price
        Double fare = pricingStrategyManager.calculateFare(request);
        Double distance = fare / 10.0; // Rough estimation back from fare for storage

        // Create Trip
        Trip trip = new Trip();
        trip.setRider(rider);
        trip.setDriver(matchedDriver);
        trip.setPickupLocation(pickup);
        trip.setDropLocation(new Location(request.getDropLat(), request.getDropLng()));
        trip.setDistance(distance);
        trip.setFare(fare);
        trip.setStatus(TripStatus.CONFIRMED);

        // Lock Driver
        matchedDriver.setStatus(DriverStatus.ON_TRIP);
        driverRepository.save(matchedDriver);

        return modelMapper.map(tripRepository.save(trip), TripDto.class);
    }

    /**
     * 3. Start Ride
     */
    @Transactional
    public TripDto startTrip(Long tripId) {
        Trip trip = getTripByIdInternal(tripId);

        if (trip.getStatus() != TripStatus.CONFIRMED) {
            throw new IllegalStateException("Trip is not in CONFIRMED state.");
        }

        trip.setStatus(TripStatus.STARTED);
        trip.setStartTime(LocalDateTime.now());

        return modelMapper.map(tripRepository.save(trip), TripDto.class);
    }

    /**
     * 4. End Ride
     */
    @Transactional
    public TripDto endTrip(Long tripId) {
        Trip trip = getTripByIdInternal(tripId);

        if (trip.getStatus() != TripStatus.STARTED) {
            throw new IllegalStateException("Trip is not STARTED.");
        }

        trip.setStatus(TripStatus.COMPLETED);
        trip.setEndTime(LocalDateTime.now());

        // Unlock Driver
        Driver driver = trip.getDriver();
        driver.setStatus(DriverStatus.ONLINE);
        driverRepository.save(driver);

        return modelMapper.map(tripRepository.save(trip), TripDto.class);
    }

    /**
     * 5. Cancel Ride
     */
    @Transactional
    public TripDto cancelTrip(Long tripId) {
        Trip trip = getTripByIdInternal(tripId);

        if (trip.getStatus() == TripStatus.COMPLETED || trip.getStatus() == TripStatus.CANCELLED) {
            throw new IllegalStateException("Cannot cancel an already completed/cancelled trip.");
        }

        trip.setStatus(TripStatus.CANCELLED);

        // Unlock Driver if assigned
        if (trip.getDriver() != null) {
            Driver driver = trip.getDriver();
            driver.setStatus(DriverStatus.ONLINE);
            driverRepository.save(driver);
        }

        return modelMapper.map(tripRepository.save(trip), TripDto.class);
    }

    /**
     * 6. Get History
     */
    public Page<TripDto> getHistory(String email, Pageable pageable) {
        // Try to find Rider first
        return riderRepository.findByEmail(email).map(rider ->
                tripRepository.findByRider(rider, pageable).map(trip -> modelMapper.map(trip, TripDto.class))
        ).orElseGet(() ->
                // If not Rider, assume Driver
                driverRepository.findByEmail(email).map(driver ->
                        tripRepository.findByDriver(driver, pageable).map(trip -> modelMapper.map(trip, TripDto.class))
                ).orElseThrow(() -> new ResourceNotFoundException("User not found: " + email))
        );
    }

    /**
     * 7. Get Trip Details
     */
    public TripDto getTripById(Long tripId) {
        return modelMapper.map(getTripByIdInternal(tripId), TripDto.class);
    }

    private Trip getTripByIdInternal(Long tripId) {
        return tripRepository.findById(tripId)
                .orElseThrow(() -> new ResourceNotFoundException("Trip not found with id: " + tripId));
    }
}
