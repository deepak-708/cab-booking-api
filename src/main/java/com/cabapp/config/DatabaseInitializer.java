package com.cabapp.config;


import com.cabapp.entity.Driver;
import com.cabapp.entity.Rider;
import com.cabapp.enums.DriverStatus;
import com.cabapp.enums.Role;
import com.cabapp.repository.DriverRepository;
import com.cabapp.repository.RiderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Collections;

@Component
@RequiredArgsConstructor
public class DatabaseInitializer implements CommandLineRunner {

    private final RiderRepository riderRepository;
    private final DriverRepository driverRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) {
        // 1. Initialize RIDER
        if (!riderRepository.findByEmail("rider@test.com").isPresent()) {
            Rider rider = new Rider();
            rider.setName("Test Rider");
            rider.setEmail("rider@test.com");
            rider.setPhone("9999999999");
            rider.setPassword(passwordEncoder.encode("password")); // Hash "password"
            rider.setRoles(Collections.singleton(Role.RIDER));

            riderRepository.save(rider);
            System.out.println("--> RIDER initialized: rider@test.com");
        }
        if (!riderRepository.findByEmail("rider2@test.com").isPresent()) {
            Rider rider = new Rider();
            rider.setName("Test Rider");
            rider.setEmail("rider2@test.com");
            rider.setPhone("9999999998");
            rider.setPassword(passwordEncoder.encode("password123")); // Hash "password"
            rider.setRoles(Collections.singleton(Role.RIDER));

            riderRepository.save(rider);
            System.out.println("--> RIDER initialized: rider2@test.com");
        }

        // 2. Initialize DRIVER
        if (!driverRepository.findByEmail("driver@test.com").isPresent()) {
            Driver driver = new Driver();
            driver.setName("Test Driver");
            driver.setEmail("driver@test.com");
            driver.setPhone("8888888888");
            driver.setPassword(passwordEncoder.encode("password")); // Hash "password"
            driver.setRoles(Collections.singleton(Role.DRIVER));

            // Driver Specifics
            driver.setVehicleId("KA01-AB-1234");
            driver.setStatus(DriverStatus.ONLINE);
            driver.setRating(4.9);
            driver.setLatitude(12.9716);
            driver.setLongitude(77.5946);

            driverRepository.save(driver);
            System.out.println("--> DRIVER initialized: driver@test.com");
        }
        if (!driverRepository.findByEmail("driver2@test.com").isPresent()) {
            Driver driver = new Driver();
            driver.setName("Test Driver 2");
            driver.setEmail("driver2@test.com");
            driver.setPhone("8888888889");
            driver.setPassword(passwordEncoder.encode("password321")); // Hash "password"
            driver.setRoles(Collections.singleton(Role.DRIVER));

            // Driver Specifics
            driver.setVehicleId("Dl-1234");
            driver.setStatus(DriverStatus.ONLINE);
            driver.setRating(4.9);
            driver.setLatitude(100.00);
            driver.setLongitude(100.00);

            driverRepository.save(driver);
            System.out.println("--> DRIVER initialized: driver2@test.com");
        }
    }
}