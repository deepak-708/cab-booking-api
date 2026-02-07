package com.cabapp.entity;

import com.cabapp.enums.DriverStatus;
import com.cabapp.enums.Role;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.Set;

@Entity
@Getter @Setter @NoArgsConstructor @AllArgsConstructor
@Table(name = "drivers", indexes = {
        @Index(name = "idx_driver_email", columnList = "email"),
        @Index(name = "idx_driver_status", columnList = "status")
})
public class Driver {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(unique = true, nullable = false)
    private String email; // Acts as Username

    @Column(nullable = false)
    private String password;

    @Column(nullable = false, unique = true)
    private String phone;

    @Column(nullable = false)
    private String vehicleId; // License Plate or ID

    private Double rating; // Defaults to 5.0

    @Enumerated(EnumType.STRING)
    private DriverStatus status;

    // Location Coordinates (Updated frequently)
    private Double latitude;
    private Double longitude;

    @ElementCollection(fetch = FetchType.EAGER)
    @Enumerated(EnumType.STRING)
    private Set<Role> roles;

    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;
}
