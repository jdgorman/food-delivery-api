package com.jdgorman.fooddeliveryapi.service;

import com.jdgorman.fooddeliveryapi.dto.DriverRequest;
import com.jdgorman.fooddeliveryapi.dto.DriverResponse;
import com.jdgorman.fooddeliveryapi.dto.DriverStatusUpdateRequest;
import com.jdgorman.fooddeliveryapi.entity.Driver;
import com.jdgorman.fooddeliveryapi.enumerator.DriverStatus;
import com.jdgorman.fooddeliveryapi.exception.ResourceNotFoundException;
import com.jdgorman.fooddeliveryapi.repository.DriverRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class DriverServiceTest {

    @Mock
    private DriverRepository driverRepository;

    @InjectMocks
    private DriverService driverService;

    private Driver driver;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        driver = Driver.builder()
                .id(1L)
                .firstName("Jamie")
                .lastName("Wells")
                .email("jamie.wells@example.com")
                .phone("555-555-0101")
                .vehicleType("Car")
                .status(DriverStatus.AVAILABLE)
                .build();
    }

    @Test
    void shouldCreateDriverSuccessfully() {
        DriverRequest request = DriverRequest.builder()
                .firstName("Jamie")
                .lastName("Wells")
                .email("jamie.wells@example.com")
                .phone("555-555-0101")
                .vehicleType("Car")
                .build();

        when(driverRepository.save(any(Driver.class))).thenAnswer(invocation -> {
            Driver saved = invocation.getArgument(0);
            saved.setId(1L);
            return saved;
        });
        when(driverRepository.countActiveDeliveriesByDriverId(1L)).thenReturn(0L);
        when(driverRepository.countTotalDeliveriesByDriverId(1L)).thenReturn(0L);

        DriverResponse result = driverService.createDriver(request);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("Jamie", result.getFirstName());
        assertEquals("Wells", result.getLastName());
    }

    @Test
    void shouldGetDriverByIdSuccessfully() {
        when(driverRepository.findById(1L)).thenReturn(Optional.of(driver));
        when(driverRepository.countActiveDeliveriesByDriverId(1L)).thenReturn(2L);
        when(driverRepository.countTotalDeliveriesByDriverId(1L)).thenReturn(10L);

        DriverResponse result = driverService.getDriverById(1L);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("Jamie", result.getFirstName());
        assertEquals(2, result.getActiveDeliveries());
        assertEquals(10, result.getTotalDeliveries());
    }

    @Test
    void shouldThrowWhenGettingNonExistentDriverById() {
        when(driverRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> driverService.getDriverById(1L));
    }

    @Test
    void shouldGetAllDriversSuccessfully() {
        when(driverRepository.findAll()).thenReturn(List.of(driver));
        when(driverRepository.countActiveDeliveriesByDriverId(1L)).thenReturn(1L);
        when(driverRepository.countTotalDeliveriesByDriverId(1L)).thenReturn(5L);

        List<DriverResponse> result = driverService.getAllDrivers();

        assertEquals(1, result.size());
        assertEquals("Jamie", result.get(0).getFirstName());
        assertEquals(1, result.get(0).getActiveDeliveries());
        assertEquals(5, result.get(0).getTotalDeliveries());
    }

    @Test
    void shouldUpdateDriverStatusSuccessfully() {
        DriverStatusUpdateRequest request = DriverStatusUpdateRequest.builder()
                .status(DriverStatus.BUSY)
                .build();

        when(driverRepository.findById(1L)).thenReturn(Optional.of(driver));
        when(driverRepository.save(any(Driver.class))).thenAnswer(invocation -> invocation.getArgument(0));
        when(driverRepository.countActiveDeliveriesByDriverId(1L)).thenReturn(0L);
        when(driverRepository.countTotalDeliveriesByDriverId(1L)).thenReturn(0L);

        DriverResponse result = driverService.updateDriverStatus(1L, request);

        assertEquals(DriverStatus.BUSY, result.getStatus());
    }

    @Test
    void shouldThrowWhenUpdatingStatusOfNonExistentDriver() {
        DriverStatusUpdateRequest request = DriverStatusUpdateRequest.builder()
                .status(DriverStatus.BUSY)
                .build();

        when(driverRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> driverService.updateDriverStatus(1L, request));
    }
}
