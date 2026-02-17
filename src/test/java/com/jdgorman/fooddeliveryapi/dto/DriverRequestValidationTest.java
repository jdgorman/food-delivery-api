package com.jdgorman.fooddeliveryapi.dto;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class DriverRequestValidationTest {

    private Validator validator;

    @BeforeEach
    void setUp() {
        LocalValidatorFactoryBean factoryBean = new LocalValidatorFactoryBean();
        factoryBean.afterPropertiesSet();
        validator = factoryBean.getValidator();
    }

    @Test
    void shouldPassValidationWhenAllFieldsAreValid() {
        DriverRequest request = DriverRequest.builder()
                .firstName("John")
                .lastName("Doe")
                .email("john.doe@example.com")
                .phone("123-456-7890")
                .vehicleType("Car")
                .licensePlate("ABC123")
                .build();

        Set<ConstraintViolation<DriverRequest>> violations = validator.validate(request);

        assertTrue(violations.isEmpty());
    }

    @Test
    void shouldFailValidationWhenFirstNameIsBlank() {
        DriverRequest request = DriverRequest.builder()
                .lastName("Doe")
                .email("john.doe@example.com")
                .phone("123-456-7890")
                .vehicleType("Car")
                .licensePlate("ABC123")
                .build();

        Set<ConstraintViolation<DriverRequest>> violations = validator.validate(request);

        assertEquals(1, violations.size());
        assertEquals("First name is required", violations.iterator().next().getMessage());
    }

    @Test
    void shouldFailValidationWhenLastNameIsBlank() {
        DriverRequest request = DriverRequest.builder()
                .firstName("John")
                .email("john.doe@example.com")
                .phone("123-456-7890")
                .vehicleType("Car")
                .licensePlate("ABC123")
                .build();

        Set<ConstraintViolation<DriverRequest>> violations = validator.validate(request);

        assertEquals(1, violations.size());
        assertEquals("Last name is required", violations.iterator().next().getMessage());
    }

    @Test
    void shouldFailValidationWhenEmailIsInvalid() {
        DriverRequest request = DriverRequest.builder()
                .firstName("John")
                .lastName("Doe")
                .email("invalid-email")
                .phone("123-456-7890")
                .vehicleType("Car")
                .licensePlate("ABC123")
                .build();

        Set<ConstraintViolation<DriverRequest>> violations = validator.validate(request);

        assertEquals(1, violations.size());
        assertEquals("Email must be valid", violations.iterator().next().getMessage());
    }

    @Test
    void shouldFailValidationWhenPhoneIsBlank() {
        DriverRequest request = DriverRequest.builder()
                .firstName("John")
                .lastName("Doe")
                .email("john.doe@example.com")
                .vehicleType("Car")
                .licensePlate("ABC123")
                .build();

        Set<ConstraintViolation<DriverRequest>> violations = validator.validate(request);

        assertEquals(1, violations.size());
        assertEquals("Phone number is required", violations.iterator().next().getMessage());
    }

    @Test
    void shouldFailValidationWhenVehicleTypeIsBlank() {
        DriverRequest request = DriverRequest.builder()
                .firstName("John")
                .lastName("Doe")
                .email("john.doe@example.com")
                .phone("123-456-7890")
                .licensePlate("ABC123")
                .build();

        Set<ConstraintViolation<DriverRequest>> violations = validator.validate(request);

        assertEquals(1, violations.size());
        assertEquals("Vehicle type is required", violations.iterator().next().getMessage());
    }

    @Test
    void shouldPassValidationWhenLicensePlateIsNull() {
        DriverRequest request = DriverRequest.builder()
                .firstName("John")
                .lastName("Doe")
                .email("john.doe@example.com")
                .phone("123-456-7890")
                .vehicleType("Car")
                .build();

        Set<ConstraintViolation<DriverRequest>> violations = validator.validate(request);

        assertTrue(violations.isEmpty());
    }
}
