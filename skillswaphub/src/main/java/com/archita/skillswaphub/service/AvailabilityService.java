package com.archita.skillswaphub.service;

import com.archita.skillswaphub.model.Availability;
import com.archita.skillswaphub.repository.AvailabilityRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class AvailabilityService {

    @Autowired
    private AvailabilityRepository availabilityRepository;

    // Get all availability slots for a user
    public List<Availability> getByUser(Long userId) {
        return availabilityRepository.findByUserId(userId);
    }

    // Save availability slots for a user
    // Replaces all existing slots with new ones
    @Transactional
    public List<Availability> saveSlots(Long userId, List<Availability> slots) {
        // Delete existing slots first
        availabilityRepository.deleteByUserId(userId);

        // Set userId on each slot and save
        for (Availability slot : slots) {
            slot.setUserId(userId);
        }
        return availabilityRepository.saveAll(slots);
    }

    // Delete a single slot
    public void deleteSlot(Long slotId) {
        availabilityRepository.deleteById(slotId);
    }
}