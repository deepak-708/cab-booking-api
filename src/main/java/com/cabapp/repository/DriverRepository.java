package com.cabapp.repository;

import com.cabapp.entity.Driver;
import com.cabapp.enums.DriverStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DriverRepository extends JpaRepository<Driver, Long> {

    Optional<Driver> findByEmail(String email);
    boolean existsByEmail(String email);

    // Find all drivers with a specific status (e.g., ONLINE)
    List<Driver> findByStatus(DriverStatus status);

    /**
     * Native SQL Query to find nearest ONLINE drivers.
     * Uses Haversine formula to calculate distance on the fly.
     * 6371 = Radius of Earth in KM.
     */
    @Query(value = "SELECT d.*, " +
            "(6371 * acos(cos(radians(:lat)) * cos(radians(d.latitude)) * " +
            "cos(radians(d.longitude) - radians(:lng)) + " +
            "sin(radians(:lat)) * sin(radians(d.latitude)))) AS distance " +
            "FROM drivers d " +
            "WHERE d.status = 'ONLINE' " +
            "HAVING distance < :radius " +
            "ORDER BY distance ASC " +
            "LIMIT 10", nativeQuery = true)
    List<Driver> findTenNearestDrivers(
            @Param("lat") Double latitude,
            @Param("lng") Double longitude,
            @Param("radius") Double radius
    );
}