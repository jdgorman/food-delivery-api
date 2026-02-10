package com.jdgorman.fooddeliveryapi.dto;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;

class DeliveryAddressResponseTest {

    @Test
    void deliveryAddressResponseBuilderCreatesObjectWithCorrectValues() {
        LocalDateTime now = LocalDateTime.now();

        DeliveryAddressResponse response = DeliveryAddressResponse.builder()
                .id(1L)
                .customerId(2L)
                .customerName("John Doe")
                .label("Home")
                .streetAddress("123 Main St")
                .city("Dallas")
                .state("TX")
                .zipCode("75201")
                .isDefault(true)
                .createdAt(now)
                .updatedAt(now)
                .build();

        assertEquals(1L, response.getId());
        assertEquals(2L, response.getCustomerId());
        assertEquals("John Doe", response.getCustomerName());
        assertEquals("Home", response.getLabel());
        assertEquals("123 Main St", response.getStreetAddress());
        assertEquals("Dallas", response.getCity());
        assertEquals("TX", response.getState());
        assertEquals("75201", response.getZipCode());
        assertEquals(true, response.getIsDefault());
        assertEquals(now, response.getCreatedAt());
        assertEquals(now, response.getUpdatedAt());
    }

    @Test
    void deliveryAddressResponseBuilderHandlesNullValues() {
        DeliveryAddressResponse response = DeliveryAddressResponse.builder()
                .id(null)
                .customerId(null)
                .customerName(null)
                .label(null)
                .streetAddress(null)
                .city(null)
                .state(null)
                .zipCode(null)
                .isDefault(null)
                .createdAt(null)
                .updatedAt(null)
                .build();

        assertEquals(null, response.getId());
        assertEquals(null, response.getCustomerId());
        assertEquals(null, response.getCustomerName());
        assertEquals(null, response.getLabel());
        assertEquals(null, response.getStreetAddress());
        assertEquals(null, response.getCity());
        assertEquals(null, response.getState());
        assertEquals(null, response.getZipCode());
        assertEquals(null, response.getIsDefault());
        assertEquals(null, response.getCreatedAt());
        assertEquals(null, response.getUpdatedAt());
    }
}
