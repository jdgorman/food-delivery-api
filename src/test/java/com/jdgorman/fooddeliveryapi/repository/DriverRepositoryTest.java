package com.jdgorman.fooddeliveryapi.repository;

import com.jdgorman.fooddeliveryapi.entity.Driver;
import com.jdgorman.fooddeliveryapi.enumerator.DriverStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class DriverRepositoryTest {

    @Autowired
    private DriverRepository driverRepository;

    private Driver driver;

    @BeforeEach
    void setUp() {
        driver = driverRepository.save(Driver.builder()
                .firstName("Alex")
                .lastName("Taylor")
                .email("alex.taylor@example.com")
                .phone("555-555-0101")
                .vehicleType("Car")
                .status(DriverStatus.AVAILABLE)
                .build());
    }

    @Test
    void shouldFindDriverByEmail() {
        Optional<Driver> result = driverRepository.findByEmail("alex.taylor@example.com");
        assertTrue(result.isPresent());
        assertEquals(driver.getId(), result.get().getId());
    }

    @Test
    void shouldReturnEmptyWhenEmailDoesNotExist() {
        Optional<Driver> result = driverRepository.findByEmail("nonexistent@example.com");
        assertTrue(result.isEmpty());
    }

    @Test
    void shouldCheckExistenceByEmail() {
        boolean exists = driverRepository.existsByEmail("alex.taylor@example.com");
        assertTrue(exists);
    }

    @Test
    void shouldReturnFalseWhenEmailDoesNotExist() {
        boolean exists = driverRepository.existsByEmail("nonexistent@example.com");
        assertFalse(exists);
    }

    @Test
    void shouldFindDriversByStatus() {
        List<Driver> result = driverRepository.findByStatus(DriverStatus.AVAILABLE);
        assertEquals(1, result.size());
        assertEquals(driver.getId(), result.get(0).getId());
    }

    @Test
    void shouldReturnEmptyListWhenStatusDoesNotMatch() {
        List<Driver> result = driverRepository.findByStatus(DriverStatus.OFFLINE);
        assertTrue(result.isEmpty());
    }
}
