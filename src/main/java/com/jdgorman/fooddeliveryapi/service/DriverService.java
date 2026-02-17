package com.jdgorman.fooddeliveryapi.service;

import com.jdgorman.fooddeliveryapi.dto.DriverRequest;
import com.jdgorman.fooddeliveryapi.dto.DriverResponse;
import com.jdgorman.fooddeliveryapi.dto.DriverStatusUpdateRequest;
import com.jdgorman.fooddeliveryapi.entity.Driver;
import com.jdgorman.fooddeliveryapi.enumerator.DriverStatus;
import com.jdgorman.fooddeliveryapi.exception.DuplicateResourceException;
import com.jdgorman.fooddeliveryapi.exception.ResourceNotFoundException;
import com.jdgorman.fooddeliveryapi.repository.DriverRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Service class for managing drivers.
 * Handles the business logic for creating, retrieving, updating, and deleting driver information.
 */
@Service
@RequiredArgsConstructor
public class DriverService {

    private final DriverRepository driverRepository;

    /**
     * Retrieves all drivers.
     *
     * @return a list of all drivers
     */
    public List<DriverResponse> getAllDrivers() {
        List<Driver> drivers = driverRepository.findAll();
        return drivers.stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    /**
     * Retrieves all available drivers.
     *
     * @return a list of available drivers
     */
    public List<DriverResponse> getAvailableDrivers() {
        List<Driver> drivers = driverRepository.findByStatus(DriverStatus.AVAILABLE);
        return drivers.stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    /**
     * Retrieves a driver by their ID.
     *
     * @param id the ID of the driver to retrieve
     * @return the driver
     */
    public DriverResponse getDriverById(Long id) {
        Driver driver = driverRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Driver with id " + id + " does not exist"
                ));
        return convertToResponse(driver);
    }

    /**
     * Creates a new driver.
     *
     * @param request the driver to create
     * @return the created driver
     */
    @Transactional
    public DriverResponse createDriver(DriverRequest request) {
        if (driverRepository.existsByEmail(request.getEmail())) {
            throw new DuplicateResourceException(
                    "Driver with email '" + request.getEmail() + "' already exists"
            );
        }

        Driver driver = Driver.builder()
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .email(request.getEmail())
                .phone(request.getPhone())
                .vehicleType(request.getVehicleType())
                .licensePlate(request.getLicensePlate())
                .status(DriverStatus.AVAILABLE)
                .build();

        Driver saved = driverRepository.save(driver);
        return convertToResponse(saved);
    }

    /**
     * Updates an existing driver.
     *
     * @param id the ID of the driver to update
     * @param request the updated driver details
     * @return the updated driver
     */
    @Transactional
    public DriverResponse updateDriver(Long id, DriverRequest request) {
        Driver driver = driverRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Cannot update. Driver with id " + id + " does not exist"
                ));

        // Check if email is being changed to one that already exists
        if (!driver.getEmail().equals(request.getEmail()) &&
                driverRepository.existsByEmail(request.getEmail())) {
            throw new DuplicateResourceException(
                    "Driver with email '" + request.getEmail() + "' already exists"
            );
        }

        driver.setFirstName(request.getFirstName());
        driver.setLastName(request.getLastName());
        driver.setEmail(request.getEmail());
        driver.setPhone(request.getPhone());
        driver.setVehicleType(request.getVehicleType());
        driver.setLicensePlate(request.getLicensePlate());

        Driver updated = driverRepository.save(driver);
        return convertToResponse(updated);
    }

    /**
     * Updates the status of an existing driver.
     *
     * @param id the ID of the driver to update
     * @param request the new status of the driver
     * @return the updated driver
     */
    @Transactional
    public DriverResponse updateDriverStatus(Long id, DriverStatusUpdateRequest request) {
        Driver driver = driverRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Cannot update status. Driver with id " + id + " does not exist"
                ));

        driver.setStatus(request.getStatus());

        Driver updated = driverRepository.save(driver);
        return convertToResponse(updated);
    }

    /**
     * Deletes a driver by their ID.
     *
     * @param id the ID of the driver to delete
     */
    public void deleteDriver(Long id) {
        if (!driverRepository.existsById(id)) {
            throw new ResourceNotFoundException(
                    "Cannot delete. Driver with id " + id + " does not exist"
            );
        }
        driverRepository.deleteById(id);
    }

    private DriverResponse convertToResponse(Driver driver) {
        long activeDeliveries = driverRepository.countActiveDeliveriesByDriverId(driver.getId());
        long totalDeliveries = driverRepository.countTotalDeliveriesByDriverId(driver.getId());

        return DriverResponse.builder()
                .id(driver.getId())
                .firstName(driver.getFirstName())
                .lastName(driver.getLastName())
                .email(driver.getEmail())
                .phone(driver.getPhone())
                .vehicleType(driver.getVehicleType())
                .licensePlate(driver.getLicensePlate())
                .status(driver.getStatus())
                .activeDeliveries((int) activeDeliveries)
                .totalDeliveries((int) totalDeliveries)
                .createTimestamp(driver.getCreateTimestamp())
                .updateTimestamp(driver.getUpdateTimestamp())
                .build();
    }
}