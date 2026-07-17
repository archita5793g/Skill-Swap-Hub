package com.archita.skillswaphub.repository;

import com.archita.skillswaphub.model.Availability;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AvailabilityRepository extends JpaRepository<Availability, Long> {

    // Get all slots for a specific user
    List<Availability> findByUserId(Long userId);

    // Delete all slots for a user (before saving new ones)
    void deleteByUserId(Long userId);
}