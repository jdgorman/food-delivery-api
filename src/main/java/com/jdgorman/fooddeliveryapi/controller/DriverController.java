package com.jdgorman.fooddeliveryapi.controller;

import com.jdgorman.fooddeliveryapi.dto.DriverRequest;
import com.jdgorman.fooddeliveryapi.dto.DriverResponse;
import com.jdgorman.fooddeliveryapi.dto.DriverStatusUpdateRequest;
import com.jdgorman.fooddeliveryapi.service.DriverService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST controller for managing drivers.
 * Provides endpoints for creating, retrieving, updating, and deleting driver information.
 */
@RestController
@RequestMapping("/api/drivers")
@RequiredArgsConstructor
public class DriverController {

    private final DriverService driverService;

    /**
     * Retrieves all drivers.
     *
     * @return a list of all driver responses
     */
    @GetMapping
    public ResponseEntity<List<DriverResponse>> getAllDrivers() {
        List<DriverResponse> drivers = driverService.getAllDrivers();
        return ResponseEntity.ok(drivers);
    }

    /**
     * Retrieves all available drivers.
     *
     * @return a list of available driver responses
     */
    @GetMapping("/available")
    public ResponseEntity<List<DriverResponse>> getAvailableDrivers() {
        List<DriverResponse> drivers = driverService.getAvailableDrivers();
        return ResponseEntity.ok(drivers);
    }

    /**
     * Retrieves a driver by their ID.
     *
     * @param id the ID of the driver to retrieve
     * @return the driver response
     */
    @GetMapping("/{id}")
    public ResponseEntity<DriverResponse> getDriverById(@PathVariable Long id) {
        DriverResponse driver = driverService.getDriverById(id);
        return ResponseEntity.ok(driver);
    }

    /**
     * Creates a new driver.
     *
     * @param request the driver request containing driver details
     * @return the created driver response
     */
    @PostMapping
    public ResponseEntity<DriverResponse> createDriver(@Valid @RequestBody DriverRequest request) {
        DriverResponse created = driverService.createDriver(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    /**
     * Updates an existing driver.
     *
     * @param id the ID of the driver to update
     * @param request the driver request containing updated driver details
     * @return the updated driver response
     */
    @PutMapping("/{id}")
    public ResponseEntity<DriverResponse> updateDriver(
            @PathVariable Long id,
            @Valid @RequestBody DriverRequest request) {
        DriverResponse updated = driverService.updateDriver(id, request);
        return ResponseEntity.ok(updated);
    }

    /**
     * Updates the status of an existing driver.
     *
     * @param id the ID of the driver to update
     * @param request the driver status update request containing the new status
     * @return the updated driver response
     */
    @PutMapping("/{id}/status")
    public ResponseEntity<DriverResponse> updateDriverStatus(
            @PathVariable Long id,
            @Valid @RequestBody DriverStatusUpdateRequest request) {
        DriverResponse updated = driverService.updateDriverStatus(id, request);
        return ResponseEntity.ok(updated);
    }

    /**
     * Deletes a driver by their ID.
     *
     * @param id the ID of the driver to delete
     * @return a response entity with no content
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteDriver(@PathVariable Long id) {
        driverService.deleteDriver(id);
        return ResponseEntity.noContent().build();
    }
}