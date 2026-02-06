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
        Restaurant restaurant = new Restaurant();
        restaurant.setName("Valid Restaurant");
        restaurant.setAddress("123 Main St");
        restaurant.setPhone("123-456-7890");
        restaurant.setCuisineType("Italian");
        restaurant.setActive(true);
        restaurant.setCreateTimestamp(LocalDateTime.now());

        Set<ConstraintViolation<Restaurant>> violations = validator.validate(restaurant);

        assertTrue(violations.isEmpty());
    }

    @Test
    void restaurantIsInvalidWhenNameIsBlank() {
        Restaurant restaurant = new Restaurant();
        restaurant.setName("");
        restaurant.setAddress("123 Main St");
        restaurant.setPhone("123-456-7890");
        restaurant.setCuisineType("Italian");
        restaurant.setActive(true);
        restaurant.setCreateTimestamp(LocalDateTime.now());

        Set<ConstraintViolation<Restaurant>> violations = validator.validate(restaurant);

        assertFalse(violations.isEmpty());
        assertEquals("Restaurant name is required", violations.iterator().next().getMessage());
    }

    @Test
    void restaurantIsInvalidWhenAddressIsBlank() {
        Restaurant restaurant = new Restaurant();
        restaurant.setName("Valid Restaurant");
        restaurant.setAddress("");
        restaurant.setPhone("123-456-7890");
        restaurant.setCuisineType("Italian");
        restaurant.setActive(true);
        restaurant.setCreateTimestamp(LocalDateTime.now());

        Set<ConstraintViolation<Restaurant>> violations = validator.validate(restaurant);

        assertFalse(violations.isEmpty());
        assertEquals("Address is required", violations.iterator().next().getMessage());
    }

    @Test
    void restaurantIsInvalidWhenPhoneIsInWrongFormat() {
        Restaurant restaurant = new Restaurant();
        restaurant.setName("Valid Restaurant");
        restaurant.setAddress("123 Main St");
        restaurant.setPhone("123456789");
        restaurant.setCuisineType("Italian");
        restaurant.setActive(true);
        restaurant.setCreateTimestamp(LocalDateTime.now());

        Set<ConstraintViolation<Restaurant>> violations = validator.validate(restaurant);

        assertFalse(violations.isEmpty());
        assertEquals("Phone must be a valid US phone number", violations.iterator().next().getMessage());
    }

    @Test
    void restaurantIsInvalidWhenCuisineTypeIsBlank() {
        Restaurant restaurant = new Restaurant();
        restaurant.setName("Valid Restaurant");
        restaurant.setAddress("123 Main St");
        restaurant.setPhone("123-456-7890");
        restaurant.setCuisineType("");
        restaurant.setActive(true);
        restaurant.setCreateTimestamp(LocalDateTime.now());

        Set<ConstraintViolation<Restaurant>> violations = validator.validate(restaurant);

        assertFalse(violations.isEmpty());
        assertEquals("Cuisine type is required", violations.iterator().next().getMessage());
    }

    @Test
    void createDateIsSetAutomaticallyWhenNull() {
        Restaurant restaurant = new Restaurant();
        restaurant.setName("Valid Restaurant");
        restaurant.setAddress("123 Main St");
        restaurant.setPhone("123-456-7890");
        restaurant.setCuisineType("Italian");
        restaurant.setActive(true);

        restaurant.onCreate();

        assertNotNull(restaurant.getCreateTimestamp());
    }

    @Test
    void activeIsSetToTrueWhenNull() {
        Restaurant restaurant = new Restaurant();
        restaurant.setName("Valid Restaurant");
        restaurant.setAddress("123 Main St");
        restaurant.setPhone("123-456-7890");
        restaurant.setCuisineType("Italian");
        restaurant.setActive(null);

        restaurant.onCreate();

        assertTrue(restaurant.getActive());
    }
}
