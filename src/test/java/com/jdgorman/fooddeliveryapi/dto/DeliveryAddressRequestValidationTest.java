package com.jdgorman.fooddeliveryapi.dto;

import org.junit.jupiter.api.Test;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import jakarta.validation.ConstraintViolation;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class DeliveryAddressRequestValidationTest {

    private final Validator validator;

    public DeliveryAddressRequestValidationTest() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        this.validator = factory.getValidator();
    }

    @Test
    void validDeliveryAddressRequestPassesValidation() {
        DeliveryAddressRequest request = DeliveryAddressRequest.builder()
                .label("Home")
                .streetAddress("123 Main St")
                .city("Dallas")
                .state("TX")
                .zipCode("75201")
                .isDefault(true)
                .build();

        Set<ConstraintViolation<DeliveryAddressRequest>> violations = validator.validate(request);

        assertTrue(violations.isEmpty());
    }

    @Test
    void missingLabelFailsValidation() {
        DeliveryAddressRequest request = DeliveryAddressRequest.builder()
                .streetAddress("123 Main St")
                .city("Dallas")
                .state("TX")
                .zipCode("75201")
                .isDefault(true)
                .build();

        Set<ConstraintViolation<DeliveryAddressRequest>> violations = validator.validate(request);

        assertEquals(1, violations.size());
        assertEquals("Address label is required", violations.iterator().next().getMessage());
    }

    @Test
    void missingStreetAddressFailsValidation() {
        DeliveryAddressRequest request = DeliveryAddressRequest.builder()
                .label("Home")
                .city("Dallas")
                .state("TX")
                .zipCode("75201")
                .isDefault(true)
                .build();

        Set<ConstraintViolation<DeliveryAddressRequest>> violations = validator.validate(request);

        assertEquals(1, violations.size());
        assertEquals("Street address is required", violations.iterator().next().getMessage());
    }

    @Test
    void missingCityFailsValidation() {
        DeliveryAddressRequest request = DeliveryAddressRequest.builder()
                .label("Home")
                .streetAddress("123 Main St")
                .state("TX")
                .zipCode("75201")
                .isDefault(true)
                .build();

        Set<ConstraintViolation<DeliveryAddressRequest>> violations = validator.validate(request);

        assertEquals(1, violations.size());
        assertEquals("City is required", violations.iterator().next().getMessage());
    }

    @Test
    void missingStateFailsValidation() {
        DeliveryAddressRequest request = DeliveryAddressRequest.builder()
                .label("Home")
                .streetAddress("123 Main St")
                .city("Dallas")
                .zipCode("75201")
                .isDefault(true)
                .build();

        Set<ConstraintViolation<DeliveryAddressRequest>> violations = validator.validate(request);

        assertEquals(1, violations.size());
        assertEquals("State is required", violations.iterator().next().getMessage());
    }

    @Test
    void missingZipCodeFailsValidation() {
        DeliveryAddressRequest request = DeliveryAddressRequest.builder()
                .label("Home")
                .streetAddress("123 Main St")
                .city("Dallas")
                .state("TX")
                .isDefault(true)
                .build();

        Set<ConstraintViolation<DeliveryAddressRequest>> violations = validator.validate(request);

        assertEquals(1, violations.size());
        assertEquals("Zip code is required", violations.iterator().next().getMessage());
    }
}
