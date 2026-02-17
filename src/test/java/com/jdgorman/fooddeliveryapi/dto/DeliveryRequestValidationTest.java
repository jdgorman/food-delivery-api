package com.jdgorman.fooddeliveryapi.dto;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;
import java.time.LocalDateTime;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class DeliveryRequestValidationTest {

    private Validator validator;

    @BeforeEach
    void setUp() {
        LocalValidatorFactoryBean factoryBean = new LocalValidatorFactoryBean();
        factoryBean.afterPropertiesSet();
        validator = factoryBean.getValidator();
    }

    @Test
    void shouldPassValidationWhenAllFieldsAreValid() {
        DeliveryRequest request = DeliveryRequest.builder()
                .orderId(1L)
                .driverId(1L)
                .estimatedDeliveryTime(LocalDateTime.now().plusHours(1))
                .build();

        Set<ConstraintViolation<DeliveryRequest>> violations = validator.validate(request);

        assertTrue(violations.isEmpty());
    }

    @Test
    void shouldFailValidationWhenOrderIdIsNull() {
        DeliveryRequest request = DeliveryRequest.builder()
                .driverId(1L)
                .estimatedDeliveryTime(LocalDateTime.now().plusHours(1))
                .build();

        Set<ConstraintViolation<DeliveryRequest>> violations = validator.validate(request);

        assertEquals(1, violations.size());
        assertEquals("Order ID is required", violations.iterator().next().getMessage());
    }

    @Test
    void shouldFailValidationWhenDriverIdIsNull() {
        DeliveryRequest request = DeliveryRequest.builder()
                .orderId(1L)
                .estimatedDeliveryTime(LocalDateTime.now().plusHours(1))
                .build();

        Set<ConstraintViolation<DeliveryRequest>> violations = validator.validate(request);

        assertEquals(1, violations.size());
        assertEquals("Driver ID is required", violations.iterator().next().getMessage());
    }

    @Test
    void shouldPassValidationWhenEstimatedDeliveryTimeIsNull() {
        DeliveryRequest request = DeliveryRequest.builder()
                .orderId(1L)
                .driverId(1L)
                .build();

        Set<ConstraintViolation<DeliveryRequest>> violations = validator.validate(request);

        assertTrue(violations.isEmpty());
    }
}
