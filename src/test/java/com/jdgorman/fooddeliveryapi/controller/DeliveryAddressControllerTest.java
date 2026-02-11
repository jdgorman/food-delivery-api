package com.jdgorman.fooddeliveryapi.controller;

import com.jdgorman.fooddeliveryapi.dto.DeliveryAddressRequest;
import com.jdgorman.fooddeliveryapi.dto.DeliveryAddressResponse;
import com.jdgorman.fooddeliveryapi.service.DeliveryAddressService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class DeliveryAddressControllerTest {

    @Mock
    private DeliveryAddressService deliveryAddressService;

    @InjectMocks
    private DeliveryAddressController deliveryAddressController;

    private AutoCloseable closeable;

    @BeforeEach
    void setUp() {
        closeable = MockitoAnnotations.openMocks(this);
    }

    @AfterEach
    void tearDown() throws Exception {
        if (closeable != null) closeable.close();
    }

    @Test
    void getAllDeliveryAddressesReturnsListOfAddresses() {
        Long customerId = 1L;
        List<DeliveryAddressResponse> mockResponses = Arrays.asList(
                DeliveryAddressResponse.builder().id(1L).streetAddress("123 Main St").city("Dallas").state("TX").zipCode("75201").build(),
                DeliveryAddressResponse.builder().id(2L).streetAddress("456 Elm St").city("Austin").state("TX").zipCode("73301").build()
        );
        when(deliveryAddressService.getDeliveryAddressesByCustomer(customerId)).thenReturn(mockResponses);

        var response = deliveryAddressController.getAddressesByCustomer(customerId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(mockResponses, response.getBody());
        verify(deliveryAddressService, times(1)).getDeliveryAddressesByCustomer(customerId);
    }

    @Test
    void createDeliveryAddressReturnsCreatedAddress() {
        Long customerId = 1L;
        DeliveryAddressRequest request = DeliveryAddressRequest.builder()
                .label("Home")
                .streetAddress("123 Main St")
                .city("Dallas")
                .state("TX")
                .zipCode("75201")
                .isDefault(false)
                .build();

        DeliveryAddressResponse mockResponse = DeliveryAddressResponse.builder()
                .id(1L)
                .streetAddress("123 Main St")
                .city("Dallas")
                .state("TX")
                .zipCode("75201")
                .build();

        when(deliveryAddressService.createDeliveryAddress(customerId, request)).thenReturn(mockResponse);

        var response = deliveryAddressController.createAddress(customerId, request);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(mockResponse, response.getBody());
        verify(deliveryAddressService, times(1)).createDeliveryAddress(customerId, request);
    }

    @Test
    void deleteDeliveryAddressReturnsNoContent() {
        Long customerId = 1L;
        Long addressId = 1L;

        var response = deliveryAddressController.deleteAddress(customerId, addressId);

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        verify(deliveryAddressService, times(1)).deleteDeliveryAddress(customerId, addressId);
    }
}
