package com.cabapp.controller;

import com.cabapp.dto.RideRequestDto;
import com.cabapp.dto.TripDto;
import com.cabapp.service.TripService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@RequestMapping("/api/trips")
@RequiredArgsConstructor
@Tag(name = "Trip APIs", description = "Ride Booking & Lifecycle")
public class TripController {

    private final TripService tripService;

    // 1. ESTIMATE (Public)
    @Operation(summary = "Estimate Fare", description = "Get approximate fare without booking")
    @PostMapping("/calculate")
    public ResponseEntity<Double> estimateTrip(@Valid @RequestBody RideRequestDto request) {
        return ResponseEntity.ok(tripService.calculateEstimate(request));
    }

    // 2. BOOK (Rider Only)
    @Operation(summary = "Book Ride", description = "Book a ride (Locks price & assigns driver)")
    @PostMapping("/book")
    @PreAuthorize("hasRole('RIDER')")
    public ResponseEntity<TripDto> bookTrip(@Valid @RequestBody RideRequestDto request, Principal principal) {
        return ResponseEntity.ok(tripService.requestTrip(request, principal.getName()));
    }

    // 3. START (Driver Only)
    @Operation(summary = "Start Ride", description = "Driver starts the trip")
    @PostMapping("/{tripId}/start")
    @PreAuthorize("hasRole('DRIVER')")
    public ResponseEntity<TripDto> startTrip(@PathVariable Long tripId) {
        return ResponseEntity.ok(tripService.startTrip(tripId));
    }

    // 4. END (Driver Only)
    @Operation(summary = "End Ride", description = "Driver completes the trip")
    @PostMapping("/{tripId}/end")
    @PreAuthorize("hasRole('DRIVER')")
    public ResponseEntity<TripDto> endTrip(@PathVariable Long tripId) {
        return ResponseEntity.ok(tripService.endTrip(tripId));
    }

    // 5. CANCEL (Any)
    @Operation(summary = "Cancel Ride", description = "Cancel trip (User or Driver)")
    @PostMapping("/{tripId}/cancel")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<TripDto> cancelTrip(@PathVariable Long tripId) {
        return ResponseEntity.ok(tripService.cancelTrip(tripId));
    }

    // 6. HISTORY
    @Operation(summary = "Trip History", description = "Get past rides (Paginated)")
    @GetMapping
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<Page<TripDto>> getHistory(
            Principal principal,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        PageRequest pageRequest = PageRequest.of(page, size, Sort.by("createdAt").descending());
        return ResponseEntity.ok(tripService.getHistory(principal.getName(), pageRequest));
    }

    // 7. DETAILS
    @Operation(summary = "Trip Details", description = "Get single trip info")
    @GetMapping("/{tripId}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<TripDto> getTripDetails(@PathVariable Long tripId) {
        return ResponseEntity.ok(tripService.getTripById(tripId));
    }
}
