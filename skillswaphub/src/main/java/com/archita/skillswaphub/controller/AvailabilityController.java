package com.archita.skillswaphub.controller;

import com.archita.skillswaphub.model.Availability;
import com.archita.skillswaphub.service.AvailabilityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/availability")
@CrossOrigin(origins = "*")
public class AvailabilityController {

    @Autowired
    private AvailabilityService availabilityService;

    // Get all availability slots for a user
    @GetMapping("/{userId}")
    public List<Availability> getAvailability(@PathVariable Long userId) {
        return availabilityService.getByUser(userId);
    }

    // Save all availability slots for a user
    @PostMapping("/{userId}")
    public List<Availability> saveAvailability(
            @PathVariable Long userId,
            @RequestBody List<Availability> slots) {

        return availabilityService.saveSlots(userId, slots);
    }

    // Delete one availability slot
    @DeleteMapping("/{slotId}")
    public String deleteSlot(@PathVariable Long slotId) {
        availabilityService.deleteSlot(slotId);
        return "Availability slot deleted successfully";
    }
}