package com.jdgorman.fooddeliveryapi.dto;

import org.junit.jupiter.api.Test;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import jakarta.validation.ConstraintViolation;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class CustomerRequestValidationTest {

    private final Validator validator;

    public CustomerRequestValidationTest() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        this.validator = factory.getValidator();
    }

    @Test
    void validCustomerRequestPassesValidation() {
        CustomerRequest request = CustomerRequest.builder()
                .firstName("John")
                .lastName("Doe")
                .email("john.doe@example.com")
                .phone("123-456-7890")
                .build();

        Set<ConstraintViolation<CustomerRequest>> violations = validator.validate(request);

        assertTrue(violations.isEmpty());
    }

    @Test
    void missingFirstNameFailsValidation() {
        CustomerRequest request = CustomerRequest.builder()
                .lastName("Doe")
                .email("john.doe@example.com")
                .phone("123-456-7890")
                .build();

        Set<ConstraintViolation<CustomerRequest>> violations = validator.validate(request);

        assertEquals(1, violations.size());
        assertEquals("First name is required", violations.iterator().next().getMessage());
    }

    @Test
    void missingLastNameFailsValidation() {
        CustomerRequest request = CustomerRequest.builder()
                .firstName("John")
                .email("john.doe@example.com")
                .phone("123-456-7890")
                .build();

        Set<ConstraintViolation<CustomerRequest>> violations = validator.validate(request);

        assertEquals(1, violations.size());
        assertEquals("Last name is required", violations.iterator().next().getMessage());
    }

    @Test
    void invalidEmailFailsValidation() {
        CustomerRequest request = CustomerRequest.builder()
                .firstName("John")
                .lastName("Doe")
                .email("invalid-email")
                .phone("123-456-7890")
                .build();

        Set<ConstraintViolation<CustomerRequest>> violations = validator.validate(request);

        assertEquals(1, violations.size());
        assertEquals("Email must be valid", violations.iterator().next().getMessage());
    }

    @Test
    void missingPhoneFailsValidation() {
        CustomerRequest request = CustomerRequest.builder()
                .firstName("John")
                .lastName("Doe")
                .email("john.doe@example.com")
                .build();

        Set<ConstraintViolation<CustomerRequest>> violations = validator.validate(request);

        assertEquals(1, violations.size());
        assertEquals("Phone number is required", violations.iterator().next().getMessage());
    }
}
