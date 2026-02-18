package com.jdgorman.fooddeliveryapi.dto;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class DriverStatusUpdateRequestValidationTest {

    private Validator validator;

    @BeforeEach
    void setUp() {
        LocalValidatorFactoryBean factoryBean = new LocalValidatorFactoryBean();
        factoryBean.afterPropertiesSet();
        validator = factoryBean.getValidator();
    }

    @Test
    void shouldPassValidationWhenStatusIsNotNull() {
        DriverStatusUpdateRequest request = DriverStatusUpdateRequest.builder()
                .status(com.jdgorman.fooddeliveryapi.enumerator.DriverStatus.AVAILABLE)
                .build();

        Set<ConstraintViolation<DriverStatusUpdateRequest>> violations = validator.validate(request);

        assertTrue(violations.isEmpty());
    }

    @Test
    void shouldFailValidationWhenStatusIsNull() {
        DriverStatusUpdateRequest request = DriverStatusUpdateRequest.builder()
                .status(null)
                .build();

        Set<ConstraintViolation<DriverStatusUpdateRequest>> violations = validator.validate(request);

        assertEquals(1, violations.size());
        assertEquals("Driver status is required", violations.iterator().next().getMessage());
    }
}
