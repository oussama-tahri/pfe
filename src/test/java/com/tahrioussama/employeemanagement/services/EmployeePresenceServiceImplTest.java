package com.tahrioussama.employeemanagement.services;

import com.tahrioussama.employeemanagement.entities.Employee;
import com.tahrioussama.employeemanagement.entities.Presence;
import com.tahrioussama.employeemanagement.repositories.PresenceRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class EmployeePresenceServiceImplTest {

    @Mock
    private PresenceRepository presenceRepository;

    @InjectMocks
    private EmployeePresenceServiceImpl presenceService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void testUpdatePresenceValueForDate() {
        // Mock presence data
        long presenceId = 1L;
        LocalDate date = LocalDate.of(2024, 3, 18);
        boolean newValue = true;

        // Mock presence entity
        Presence presence = new Presence();
        presence.setId(presenceId);
        presence.setDate(date);
        presence.setPresent(false); // Initial value

        // Mock presence repository behavior
        when(presenceRepository.findById(presenceId)).thenReturn(Optional.of(presence));
        when(presenceRepository.save(any(Presence.class))).thenReturn(presence);

        // Call the method under test
        presenceService.updatePresenceValueForDate(presenceId, date, newValue);

        // Verify that presence entity was updated and saved
        assertEquals(newValue, presence.isPresent());
        verify(presenceRepository, times(1)).save(presence);
    }
}

