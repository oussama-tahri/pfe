package com.tahrioussama.employeemanagement.services;

import com.tahrioussama.employeemanagement.entities.Presence;
import com.tahrioussama.employeemanagement.repositories.PresenceRepository;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Optional;

@Service
public class EmployeePresenceServiceImpl implements EmployeePresenceService {

    @Autowired
    private PresenceRepository presenceRepository;
    private static final Logger LOGGER = LoggerFactory.getLogger(EmployeePresenceServiceImpl.class);
    @Override
    public void updatePresenceValueForDate(Long presenceId, LocalDate date, boolean newValue) {
        // Find the presence entity by ID
        Optional<Presence> optionalPresence = presenceRepository.findById(presenceId);
        if (optionalPresence.isPresent()) {
            Presence presence = optionalPresence.get();
            // Check if the date matches
            if (presence.getDate().equals(date)) {
                // Update the presence value
                presence.setPresent(newValue);
                presenceRepository.save(presence);
            } else {
                // Date doesn't match, handle accordingly
                throw new IllegalArgumentException("Date does not match the presence entity's date.");
                // Or log an error message
                //LOGGER.error("Date does not match the presence entity's date.");
            }
        } else {
            // Presence not found, handle accordingly
            throw new IllegalArgumentException("Presence with ID " + presenceId + " not found.");
            // Or log an error message
            // LOGGER.info("Presence with ID " + presenceId + " not found.");
        }
    }
}
