package com.jdgorman.fooddeliveryapi.entity;

import org.junit.jupiter.api.Test;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;

import java.time.LocalDateTime;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class RestaurantTest {

    private final ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
    private final Validator validator = factory.getValidator();

    @Test
    void restaurantIsValidWhenAllFieldsAreCorrect() {
        Restaurant restaurant = Restaurant.builder()
                .name("Valid Restaurant")
                .address("123 Main St")
                .phone("123-456-7890")
                .cuisineType("Italian")
                .active(true)
                .createTimestamp(LocalDateTime.now())
                .build();

        Set<ConstraintViolation<Restaurant>> violations = validator.validate(restaurant);

        assertTrue(violations.isEmpty());
    }

    @Test
    void restaurantIsInvalidWhenNameIsBlank() {
        Restaurant restaurant = Restaurant.builder()
                .name("")
                .address("123 Main St")
                .phone("123-456-7890")
                .cuisineType("Italian")
                .active(true)
                .createTimestamp(LocalDateTime.now())
                .build();

        Set<ConstraintViolation<Restaurant>> violations = validator.validate(restaurant);

        assertFalse(violations.isEmpty());
        assertEquals("Restaurant name is required", violations.iterator().next().getMessage());
    }

    @Test
    void restaurantIsInvalidWhenAddressIsBlank() {
        Restaurant restaurant = Restaurant.builder()
                .name("Valid Restaurant")
                .address("")
                .phone("123-456-7890")
                .cuisineType("Italian")
                .active(true)
                .createTimestamp(LocalDateTime.now())
                .build();

        Set<ConstraintViolation<Restaurant>> violations = validator.validate(restaurant);

        assertFalse(violations.isEmpty());
        assertEquals("Address is required", violations.iterator().next().getMessage());
    }

    @Test
    void restaurantIsInvalidWhenPhoneIsInWrongFormat() {
        Restaurant restaurant = Restaurant.builder()
                .name("Valid Restaurant")
                .address("123 Main St")
                .phone("123456789")
                .cuisineType("Italian")
                .active(true)
                .createTimestamp(LocalDateTime.now())
                .build();

        Set<ConstraintViolation<Restaurant>> violations = validator.validate(restaurant);

        assertFalse(violations.isEmpty());
        assertEquals("Phone must be a valid US phone number", violations.iterator().next().getMessage());
    }

    @Test
    void restaurantIsInvalidWhenCuisineTypeIsBlank() {
        Restaurant restaurant = Restaurant.builder()
                .name("Valid Restaurant")
                .address("123 Main St")
                .phone("123-456-7890")
                .cuisineType("")
                .active(true)
                .createTimestamp(LocalDateTime.now())
                .build();

        Set<ConstraintViolation<Restaurant>> violations = validator.validate(restaurant);

        assertFalse(violations.isEmpty());
        assertEquals("Cuisine type is required", violations.iterator().next().getMessage());
    }

    @Test
    void createDateIsSetAutomaticallyWhenNull() {
        Restaurant restaurant = Restaurant.builder()
                .name("Valid Restaurant")
                .address("123 Main St")
                .phone("123-456-7890")
                .cuisineType("Italian")
                .active(true)
                .build();

        restaurant.onCreate();

        assertNotNull(restaurant.getCreateTimestamp());
    }

    @Test
    void activeIsSetToTrueWhenNull() {
        Restaurant restaurant = Restaurant.builder()
                .name("Valid Restaurant")
                .address("123 Main St")
                .phone("123-456-7890")
                .cuisineType("Italian")
                .active(null)
                .build();

        restaurant.onCreate();

        assertTrue(restaurant.getActive());
    }
}
