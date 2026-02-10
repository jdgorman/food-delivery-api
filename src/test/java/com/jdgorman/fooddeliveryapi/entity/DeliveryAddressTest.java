package com.jdgorman.fooddeliveryapi.entity;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Test;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import jakarta.validation.ConstraintViolation;

import java.time.LocalDateTime;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class DeliveryAddressTest {

    private static final ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
    private final Validator validator = factory.getValidator();

    @AfterAll
    static void closeFactory() {
        if (factory != null) {
            factory.close();
        }
    }

    @Test
    void validDeliveryAddressPassesValidation() {
        DeliveryAddress address = DeliveryAddress.builder()
                .customer(Customer.builder().build())
                .label("Home")
                .streetAddress("123 Main St")
                .city("Dallas")
                .state("TX")
                .zipCode("75201")
                .isDefault(true)
                .createTimestamp(LocalDateTime.now())
                .updateTimestamp(LocalDateTime.now())
                .build();

        Set<ConstraintViolation<DeliveryAddress>> violations = validator.validate(address);

        assertTrue(violations.isEmpty());
    }

    @Test
    void missingLabelFailsValidation() {
        DeliveryAddress address = DeliveryAddress.builder()
                .customer(Customer.builder().build())
                .streetAddress("123 Main St")
                .city("Dallas")
                .state("TX")
                .zipCode("75201")
                .isDefault(true)
                .createTimestamp(LocalDateTime.now())
                .updateTimestamp(LocalDateTime.now())
                .build();

        Set<ConstraintViolation<DeliveryAddress>> violations = validator.validate(address);

        assertEquals(1, violations.size());
        assertEquals("Address label is required", violations.iterator().next().getMessage());
    }

    @Test
    void missingStreetAddressFailsValidation() {
        DeliveryAddress address = DeliveryAddress.builder()
                .customer(Customer.builder().build())
                .label("Home")
                .city("Dallas")
                .state("TX")
                .zipCode("75201")
                .isDefault(true)
                .createTimestamp(LocalDateTime.now())
                .updateTimestamp(LocalDateTime.now())
                .build();

        Set<ConstraintViolation<DeliveryAddress>> violations = validator.validate(address);

        assertEquals(1, violations.size());
        assertEquals("Street address is required", violations.iterator().next().getMessage());
    }

    @Test
    void missingCityFailsValidation() {
        DeliveryAddress address = DeliveryAddress.builder()
                .customer(Customer.builder().build())
                .label("Home")
                .streetAddress("123 Main St")
                .state("TX")
                .zipCode("75201")
                .isDefault(true)
                .createTimestamp(LocalDateTime.now())
                .updateTimestamp(LocalDateTime.now())
                .build();

        Set<ConstraintViolation<DeliveryAddress>> violations = validator.validate(address);

        assertEquals(1, violations.size());
        assertEquals("City is required", violations.iterator().next().getMessage());
    }

    @Test
    void missingStateFailsValidation() {
        DeliveryAddress address = DeliveryAddress.builder()
                .customer(Customer.builder().build())
                .label("Home")
                .streetAddress("123 Main St")
                .city("Dallas")
                .zipCode("75201")
                .isDefault(true)
                .createTimestamp(LocalDateTime.now())
                .updateTimestamp(LocalDateTime.now())
                .build();

        Set<ConstraintViolation<DeliveryAddress>> violations = validator.validate(address);

        assertEquals(1, violations.size());
        assertEquals("State is required", violations.iterator().next().getMessage());
    }

    @Test
    void missingZipCodeFailsValidation() {
        DeliveryAddress address = DeliveryAddress.builder()
                .customer(Customer.builder().build())
                .label("Home")
                .streetAddress("123 Main St")
                .city("Dallas")
                .state("TX")
                .isDefault(true)
                .createTimestamp(LocalDateTime.now())
                .updateTimestamp(LocalDateTime.now())
                .build();

        Set<ConstraintViolation<DeliveryAddress>> violations = validator.validate(address);

        assertEquals(1, violations.size());
        assertEquals("Zip code is required", violations.iterator().next().getMessage());
    }
}
