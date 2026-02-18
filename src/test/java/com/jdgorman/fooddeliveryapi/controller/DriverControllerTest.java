package com.jdgorman.fooddeliveryapi.controller;

import com.jdgorman.fooddeliveryapi.dto.DriverRequest;
import com.jdgorman.fooddeliveryapi.dto.DriverResponse;
import com.jdgorman.fooddeliveryapi.dto.DriverStatusUpdateRequest;
import com.jdgorman.fooddeliveryapi.service.DriverService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class DriverControllerTest {

    @Mock
    private DriverService driverService;

    @InjectMocks
    private DriverController driverController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getAllDriversSuccessfully() {
        DriverResponse driver = DriverResponse.builder().id(1L).firstName("John").lastName("Doe").build();
        when(driverService.getAllDrivers()).thenReturn(Collections.singletonList(driver));

        ResponseEntity<List<DriverResponse>> result = driverController.getAllDrivers();

        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertEquals(1, result.getBody().size());
        assertEquals(driver, result.getBody().get(0));
    }

    @Test
    void getAvailableDriversSuccessfully() {
        DriverResponse driver = DriverResponse.builder().id(1L).firstName("John").lastName("Doe").build();
        when(driverService.getAvailableDrivers()).thenReturn(Collections.singletonList(driver));

        ResponseEntity<List<DriverResponse>> result = driverController.getAvailableDrivers();

        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertEquals(1, result.getBody().size());
        assertEquals(driver, result.getBody().get(0));
    }

    @Test
    void getDriverByIdSuccessfully() {
        DriverResponse driver = DriverResponse.builder().id(1L).firstName("John").lastName("Doe").build();
        when(driverService.getDriverById(1L)).thenReturn(driver);

        ResponseEntity<DriverResponse> result = driverController.getDriverById(1L);

        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertEquals(driver, result.getBody());
    }

    @Test
    void createDriverSuccessfully() {
        DriverRequest request = DriverRequest.builder().firstName("John").lastName("Doe").build();
        DriverResponse driver = DriverResponse.builder().id(1L).firstName("John").lastName("Doe").build();
        when(driverService.createDriver(request)).thenReturn(driver);

        ResponseEntity<DriverResponse> result = driverController.createDriver(request);

        assertEquals(HttpStatus.CREATED, result.getStatusCode());
        assertEquals(driver, result.getBody());
    }

    @Test
    void updateDriverSuccessfully() {
        DriverRequest request = DriverRequest.builder().firstName("John").lastName("Doe").build();
        DriverResponse driver = DriverResponse.builder().id(1L).firstName("John").lastName("Doe").build();
        when(driverService.updateDriver(1L, request)).thenReturn(driver);

        ResponseEntity<DriverResponse> result = driverController.updateDriver(1L, request);

        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertEquals(driver, result.getBody());
    }

    @Test
    void updateDriverStatusSuccessfully() {
        DriverStatusUpdateRequest request = DriverStatusUpdateRequest.builder().status(com.jdgorman.fooddeliveryapi.enumerator.DriverStatus.AVAILABLE).build();
        DriverResponse driver = DriverResponse.builder().id(1L).firstName("John").lastName("Doe").build();
        when(driverService.updateDriverStatus(1L, request)).thenReturn(driver);

        ResponseEntity<DriverResponse> result = driverController.updateDriverStatus(1L, request);

        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertEquals(driver, result.getBody());
    }

    @Test
    void deleteDriverSuccessfully() {
        doNothing().when(driverService).deleteDriver(1L);

        ResponseEntity<Void> result = driverController.deleteDriver(1L);

        assertEquals(HttpStatus.NO_CONTENT, result.getStatusCode());
        verify(driverService, times(1)).deleteDriver(1L);
    }
}
