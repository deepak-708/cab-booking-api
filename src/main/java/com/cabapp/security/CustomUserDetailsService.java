package com.cabapp.security;

import com.cabapp.entity.Driver;
import com.cabapp.entity.Rider;
import com.cabapp.repository.DriverRepository;
import com.cabapp.repository.RiderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final RiderRepository riderRepository;
    private final DriverRepository driverRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // 1. Check Rider Table
        Optional<Rider> rider = riderRepository.findByEmail(username);
        if (rider.isPresent()) {
            return User.builder()
                    .username(rider.get().getEmail())
                    .password(rider.get().getPassword())
                    .roles("RIDER")
                    .build();
        }

        // 2. Check Driver Table
        Optional<Driver> driver = driverRepository.findByEmail(username);
        if (driver.isPresent()) {
            return User.builder()
                    .username(driver.get().getEmail())
                    .password(driver.get().getPassword())
                    .roles("DRIVER")
                    .build();
        }

        // 3. User Not Found
        throw new UsernameNotFoundException("User not found with email: " + username);
    }
}
