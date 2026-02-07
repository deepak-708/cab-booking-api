package com.cabapp.controller;

import com.cabapp.dto.*;
import com.cabapp.service.DriverService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@RequestMapping("/api/drivers")
@RequiredArgsConstructor
@Tag(name = "Driver APIs", description = "Driver Management & Real-time Updates")
public class DriverController {

    private final DriverService driverService;

    // 1. SIGNUP (Public)
    @Operation(summary = "Register Driver", description = "Onboard a new driver")
    @PostMapping("/register")
    public ResponseEntity<DriverDto> registerDriver(@Valid @RequestBody DriverSignupRequestDto request) {
        return new ResponseEntity<>(driverService.registerDriver(request), HttpStatus.CREATED);
    }

    // 2. GET PROFILE (Secured)
    @Operation(summary = "Get My Profile", description = "Fetch details including current status and rating")
    @GetMapping("/profile")
    @PreAuthorize("hasRole('DRIVER')")
    public ResponseEntity<DriverDto> getMyProfile(Principal principal) {
        return ResponseEntity.ok(driverService.getMyProfile(principal.getName()));
    }

    // 3. UPDATE STATUS (Secured)
    @Operation(summary = "Update Status", description = "Toggle availability (ONLINE/OFFLINE)")
    @PatchMapping("/status")
    @PreAuthorize("hasRole('DRIVER')")
    public ResponseEntity<DriverDto> updateStatus(
            @RequestBody @Valid UpdateDriverStatusDto statusDto,
            Principal principal) {
        return ResponseEntity.ok(driverService.updateStatus(principal.getName(), statusDto));
    }

    // 4. UPDATE LOCATION (Secured)
    @Operation(summary = "Update Location", description = "Update GPS coordinates (Lat/Lng)")
    @PatchMapping("/location")
    @PreAuthorize("hasRole('DRIVER')")
    public ResponseEntity<DriverDto> updateLocation(
            @RequestBody @Valid UpdateDriverLocationDto locationDto,
            Principal principal) {
        return ResponseEntity.ok(driverService.updateLocation(principal.getName(), locationDto));
    }

    // 5. UPDATE PROFILE (Secured)
    @Operation(summary = "Update My Profile", description = "Update name, phone, or vehicle")
    @PutMapping("/profile")
    @PreAuthorize("hasRole('DRIVER')")
    public ResponseEntity<DriverDto> updateMyProfile(
            @RequestBody @Valid UpdateDriverDto updateDto,
            Principal principal) {
        return ResponseEntity.ok(driverService.updateMyProfile(principal.getName(), updateDto));
    }

    // 6. DELETE ACCOUNT (Secured)
    @Operation(summary = "Delete My Account", description = "Permanently delete the driver account")
    @DeleteMapping("/profile")
    @PreAuthorize("hasRole('DRIVER')")
    public ResponseEntity<String> deleteMyAccount(Principal principal) {
        driverService.deleteMyAccount(principal.getName());
        return ResponseEntity.ok("Driver account deleted successfully.");
    }
}
