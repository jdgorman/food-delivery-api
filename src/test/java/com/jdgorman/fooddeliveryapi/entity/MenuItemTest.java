package com.jdgorman.fooddeliveryapi.entity;

import com.jdgorman.fooddeliveryapi.enumerator.MenuCategory;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Test;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;

import java.math.BigDecimal;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class MenuItemTest {

    private static final ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
    private final Validator validator = factory.getValidator();

    @AfterAll
    static void closeFactory() {
        if (factory != null) {
            factory.close();
        }
    }

    @Test
    void menuItemBuilderCreatesValidObject() {
        Restaurant restaurant = Restaurant.builder()
                .id(1L)
                .name("Test Restaurant")
                .address("123 Test St")
                .phone("555-123-4567")
                .cuisineType("Test Cuisine")
                .build();

        MenuItem menuItem = MenuItem.builder()
                .id(1L)
                .name("Spring Rolls")
                .description("Crispy rolls with vegetables")
                .price(BigDecimal.valueOf(5.99))
                .category(MenuCategory.APPETIZER)
                .isAvailable(true)
                .restaurant(restaurant)
                .build();

        assertNotNull(menuItem);

        // (Validation is exercised in negative/null tests below.)

        assertEquals(1L, menuItem.getId());
        assertEquals("Spring Rolls", menuItem.getName());
        assertEquals("Crispy rolls with vegetables", menuItem.getDescription());
        assertEquals(BigDecimal.valueOf(5.99), menuItem.getPrice());
        assertEquals(MenuCategory.APPETIZER, menuItem.getCategory());
        assertEquals(true, menuItem.getIsAvailable());
    }

    @Test
    void menuItemBuilderHandlesNullValues() {
        Restaurant restaurant = Restaurant.builder()
                .id(1L)
                .name("Test Restaurant")
                .address("123 Test St")
                .phone("555-123-4567")
                .cuisineType("Test Cuisine")
                .build();

        MenuItem menuItem = MenuItem.builder()
                .id(null)
                .name(null)
                .description(null)
                .price(null)
                .category(null)
                .isAvailable(null)
                .restaurant(restaurant)
                .build();

        assertNotNull(menuItem);

        Set<ConstraintViolation<MenuItem>> violations = validator.validate(menuItem);
        assertFalse(violations.isEmpty());
        assertTrue(violations.stream().anyMatch(v -> v.getMessage().equals("Item name is required")));
        assertTrue(violations.stream().anyMatch(v -> v.getMessage().equals("Price is required")));
        assertTrue(violations.stream().anyMatch(v -> v.getMessage().equals("Category is required")));
    }

    @Test
    void menuItemBuilderHandlesNegativePrice() {
        Restaurant restaurant = Restaurant.builder()
                .id(1L)
                .name("Test Restaurant")
                .address("123 Test St")
                .phone("555-123-4567")
                .cuisineType("Test Cuisine")
                .build();

        MenuItem menuItem = MenuItem.builder()
                .id(2L)
                .name("Negative Price Item")
                .description("This item has a negative price")
                .price(BigDecimal.valueOf(-1.99))
                .category(MenuCategory.ENTREE)
                .isAvailable(true)
                .restaurant(restaurant)
                .build();

        assertNotNull(menuItem);

        Set<ConstraintViolation<MenuItem>> violations = validator.validate(menuItem);
        assertFalse(violations.isEmpty());
        assertTrue(violations.stream().anyMatch(v -> v.getMessage().equals("Price must be greater than 0")));
    }
}
