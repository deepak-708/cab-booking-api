package com.cabapp.service;

import com.cabapp.dto.RiderDto;
import com.cabapp.dto.RiderSignupRequestDto;
import com.cabapp.dto.UpdateRiderDto;
import com.cabapp.entity.Rider;
import com.cabapp.enums.RiderStatus;
import com.cabapp.enums.Role;
import com.cabapp.exception.ResourceNotFoundException;
import com.cabapp.exception.UserAlreadyExistsException;
import com.cabapp.repository.RiderRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;

@Service
@RequiredArgsConstructor
public class RiderService {

    private final RiderRepository riderRepository;
    private final ModelMapper modelMapper;
    private final PasswordEncoder passwordEncoder;

    /**
     * Create new Rider Account
     */
    @Transactional
    public RiderDto createRider(RiderSignupRequestDto request) {
        if (riderRepository.existsByEmail(request.getEmail())) {
            throw new UserAlreadyExistsException("Rider already exists with email: " + request.getEmail());
        }

        Rider rider = modelMapper.map(request, Rider.class);

        // Overwrite security fields explicitly
        rider.setPassword(passwordEncoder.encode(request.getPassword()));
        rider.setRoles(Set.of(Role.RIDER));
        rider.setStatus(RiderStatus.ACTIVE);
        rider.setRating(5.0);

        Rider savedRider = riderRepository.save(rider);
        return modelMapper.map(savedRider, RiderDto.class);
    }

    /**
     * Get Current Rider Profile
     */
    public RiderDto getMyProfile(String email) {
        Rider rider = getRiderByEmail(email);
        return modelMapper.map(rider, RiderDto.class);
    }

    /**
     * Update Current Rider Profile
     */
    @Transactional
    public RiderDto updateMyProfile(String email, UpdateRiderDto updateDto) {
        Rider rider = getRiderByEmail(email);

        // Update only non-null fields
        if (updateDto.getName() != null) rider.setName(updateDto.getName());
        if (updateDto.getPhone() != null) rider.setPhone(updateDto.getPhone());

        Rider updatedRider = riderRepository.save(rider);
        return modelMapper.map(updatedRider, RiderDto.class);
    }

    /**
     * Delete Current Rider Account
     */
    @Transactional
    public void deleteMyAccount(String email) {
        Rider rider = getRiderByEmail(email);
        riderRepository.delete(rider);
    }

    // Helper method to DRY (Don't Repeat Yourself)
    private Rider getRiderByEmail(String email) {
        return riderRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("Rider not found with email: " + email));
    }
}