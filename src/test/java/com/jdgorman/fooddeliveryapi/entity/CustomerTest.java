package com.jdgorman.fooddeliveryapi.entity;

import org.junit.jupiter.api.Test;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class CustomerTest {

    private final ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
    private final Validator validator = factory.getValidator();

    @Test
    void customerIsCreatedWithValidData() {
        Customer customer = Customer.builder()
                .firstName("John")
                .lastName("Doe")
                .email("john.doe@example.com")
                .phone("1234567890")
                .build();

        assertEquals("John", customer.getFirstName());
        assertEquals("Doe", customer.getLastName());
        assertEquals("john.doe@example.com", customer.getEmail());
        assertEquals("1234567890", customer.getPhone());
    }

    @Test
    void customerEmailValidationFailsForInvalidEmail() {
        Customer customer = Customer.builder()
                .firstName("John")
                .lastName("Doe")
                .email("invalid-email")
                .phone("1234567890")
                .build();

        Set<ConstraintViolation<Customer>> violations = validator.validate(customer);
        assertFalse(violations.isEmpty());
        assertTrue(violations.stream().anyMatch(v -> v.getMessage().equals("Email must be valid")));
    }

    @Test
    void customerPhoneNumberValidationFailsForInvalidPhoneNumber() {
        Customer customer = Customer.builder()
                .firstName("John")
                .lastName("Doe")
                .email("john.doe@example.com")
                .phone("invalid-phone")
                .build();

        Set<ConstraintViolation<Customer>> violations = validator.validate(customer);
        assertFalse(violations.isEmpty());
        assertTrue(violations.stream().anyMatch(v -> v.getMessage().equals("Phone must be a valid US phone number")));
    }

    @Test
    void customerFullNameReturnsCorrectly() {
        Customer customer = Customer.builder()
                .firstName("John")
                .lastName("Doe")
                .email("john.doe@example.com")
                .phone("1234567890")
                .build();

        assertEquals("John Doe", customer.getFirstName() + " " + customer.getLastName());
    }
}
