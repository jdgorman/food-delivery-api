package com.jdgorman.fooddeliveryapi.dto;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;

class CustomerResponseTest {

    @Test
    void customerResponseBuilderCreatesObjectWithCorrectValues() {
        LocalDateTime now = LocalDateTime.now();

        CustomerResponse response = CustomerResponse.builder()
                .id(1L)
                .firstName("John")
                .lastName("Doe")
                .email("john.doe@example.com")
                .phone("123-456-7890")
                .addressCount(2)
                .createdAt(now)
                .updatedAt(now)
                .build();

        assertEquals(1L, response.getId());
        assertEquals("John", response.getFirstName());
        assertEquals("Doe", response.getLastName());
        assertEquals("john.doe@example.com", response.getEmail());
        assertEquals("123-456-7890", response.getPhone());
        assertEquals(2, response.getAddressCount());
        assertEquals(now, response.getCreatedAt());
        assertEquals(now, response.getUpdatedAt());
    }

    @Test
    void customerResponseBuilderHandlesNullValues() {
        CustomerResponse response = CustomerResponse.builder()
                .id(null)
                .firstName(null)
                .lastName(null)
                .email(null)
                .phone(null)
                .addressCount(null)
                .createdAt(null)
                .updatedAt(null)
                .build();

        assertEquals(null, response.getId());
        assertEquals(null, response.getFirstName());
        assertEquals(null, response.getLastName());
        assertEquals(null, response.getEmail());
        assertEquals(null, response.getPhone());
        assertEquals(null, response.getAddressCount());
        assertEquals(null, response.getCreatedAt());
        assertEquals(null, response.getUpdatedAt());
    }
}
