package com.jdgorman.fooddeliveryapi.entity;

import com.jdgorman.fooddeliveryapi.enumerator.DriverStatus;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class DriverTest {

    @Test
    void shouldBuildDriverWithAllFields() {
        LocalDateTime now = LocalDateTime.now();
        List<Delivery> deliveries = new ArrayList<>();

        Driver driver = Driver.builder()
                .id(1L)
                .firstName("John")
                .lastName("Doe")
                .email("john.doe@example.com")
                .phone("123-456-7890")
                .vehicleType("Car")
                .licensePlate("ABC123")
                .status(DriverStatus.AVAILABLE)
                .deliveries(deliveries)
                .createTimestamp(now.minusDays(1))
                .updateTimestamp(now)
                .build();

        assertNotNull(driver);
        assertEquals(1L, driver.getId());
        assertEquals("John", driver.getFirstName());
        assertEquals("Doe", driver.getLastName());
        assertEquals("john.doe@example.com", driver.getEmail());
        assertEquals("123-456-7890", driver.getPhone());
        assertEquals("Car", driver.getVehicleType());
        assertEquals("ABC123", driver.getLicensePlate());
        assertEquals(DriverStatus.AVAILABLE, driver.getStatus());
        assertEquals(deliveries, driver.getDeliveries());
        assertEquals(now.minusDays(1), driver.getCreateTimestamp());
        assertEquals(now, driver.getUpdateTimestamp());
    }

    @Test
    void shouldBuildDriverWithMinimalFields() {
        Driver driver = Driver.builder()
                .firstName("John")
                .lastName("Doe")
                .email("john.doe@example.com")
                .phone("123-456-7890")
                .vehicleType("Car")
                .build();

        assertNotNull(driver);
        assertEquals("John", driver.getFirstName());
        assertEquals("Doe", driver.getLastName());
        assertEquals("john.doe@example.com", driver.getEmail());
        assertEquals("123-456-7890", driver.getPhone());
        assertEquals("Car", driver.getVehicleType());
        assertEquals(DriverStatus.AVAILABLE, driver.getStatus());
        assertNotNull(driver.getDeliveries());
        assertNotNull(driver.getCreateTimestamp());
        assertNotNull(driver.getUpdateTimestamp());
    }
}
