package com.cabapp.controller;

import com.cabapp.dto.RiderDto;
import com.cabapp.dto.RiderSignupRequestDto;
import com.cabapp.dto.UpdateRiderDto;
import com.cabapp.service.RiderService;
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
@RequestMapping("/api/riders")
@RequiredArgsConstructor
@Tag(name = "Rider APIs", description = "Rider Management & Profile Operations")
public class RiderController {

    private final RiderService riderService;

    // 1. SIGNUP (Public)
    @Operation(summary = "Register Rider", description = "Create a new rider account")
    @PostMapping("/register")
    public ResponseEntity<RiderDto> signup(@Valid @RequestBody RiderSignupRequestDto request) {
        RiderDto createdRider = riderService.createRider(request);
        return new ResponseEntity<>(createdRider, HttpStatus.CREATED);
    }

    // 2. GET PROFILE (Secured)
    @Operation(summary = "Get My Profile", description = "Fetch details of logged-in rider")
    @GetMapping("/profile")
    @PreAuthorize("hasRole('RIDER')")
    public ResponseEntity<RiderDto> getMyProfile(Principal principal) {
        return ResponseEntity.ok(riderService.getMyProfile(principal.getName()));
    }

    // 3. UPDATE PROFILE (Secured)
    @Operation(summary = "Update My Profile", description = "Update name or phone number")
    @PutMapping("/profile")
    @PreAuthorize("hasRole('RIDER')")
    public ResponseEntity<RiderDto> updateMyProfile(@Valid @RequestBody UpdateRiderDto updateDto, Principal principal) {
        return ResponseEntity.ok(riderService.updateMyProfile(principal.getName(), updateDto));
    }

    // 4. DELETE ACCOUNT (Secured)
    @Operation(summary = "Delete My Account", description = "Permanently delete the rider account")
    @DeleteMapping("/profile")
    @PreAuthorize("hasRole('RIDER')")
    public ResponseEntity<String> deleteMyAccount(Principal principal) {
        riderService.deleteMyAccount(principal.getName());
        return ResponseEntity.ok("Account deleted successfully.");
    }
}