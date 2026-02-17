package com.jdgorman.fooddeliveryapi.dto;

import com.jdgorman.fooddeliveryapi.enumerator.DriverStatus;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class DriverResponseTest {

    @Test
    void shouldBuildDriverResponseWithAllFields() {
        LocalDateTime now = LocalDateTime.now();

        DriverResponse response = DriverResponse.builder()
                .id(1L)
                .firstName("John")
                .lastName("Doe")
                .email("john.doe@example.com")
                .phone("123-456-7890")
                .vehicleType("Car")
                .licensePlate("ABC123")
                .status(DriverStatus.AVAILABLE)
                .activeDeliveries(2)
                .totalDeliveries(50)
                .createTimestamp(now.minusDays(1))
                .updateTimestamp(now)
                .build();

        assertNotNull(response);
        assertEquals(1L, response.getId());
        assertEquals("John", response.getFirstName());
        assertEquals("Doe", response.getLastName());
        assertEquals("john.doe@example.com", response.getEmail());
        assertEquals("123-456-7890", response.getPhone());
        assertEquals("Car", response.getVehicleType());
        assertEquals("ABC123", response.getLicensePlate());
        assertEquals(DriverStatus.AVAILABLE, response.getStatus());
        assertEquals(2, response.getActiveDeliveries());
        assertEquals(50, response.getTotalDeliveries());
        assertEquals(now.minusDays(1), response.getCreateTimestamp());
        assertEquals(now, response.getUpdateTimestamp());
    }

    @Test
    void shouldBuildDriverResponseWithMinimalFields() {
        DriverResponse response = DriverResponse.builder()
                .id(1L)
                .firstName("John")
                .lastName("Doe")
                .build();

        assertNotNull(response);
        assertEquals(1L, response.getId());
        assertEquals("John", response.getFirstName());
        assertEquals("Doe", response.getLastName());
    }
}
