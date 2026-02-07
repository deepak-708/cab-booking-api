package com.cabapp.repository;

import com.cabapp.entity.Driver;
import com.cabapp.entity.Rider;
import com.cabapp.entity.Trip;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TripRepository extends JpaRepository<Trip, Long> {
    // For History API
    Page<Trip> findByRider(Rider rider, Pageable pageable);
    Page<Trip> findByDriver(Driver driver, Pageable pageable);
}