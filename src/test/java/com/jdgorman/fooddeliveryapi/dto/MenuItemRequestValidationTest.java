package com.jdgorman.fooddeliveryapi.dto;

import com.jdgorman.fooddeliveryapi.enumerator.MenuCategory;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(MockitoExtension.class)
class MenuItemRequestValidationTest {

    private final Validator validator = Validation.buildDefaultValidatorFactory().getValidator();

    @Test
    void validationFailsWhenNameIsBlank() {
        MenuItemRequest request = MenuItemRequest.builder()
                .name(" ")
                .price(BigDecimal.valueOf(10.00))
                .category(MenuCategory.BEVERAGE)
                .build();

        Set<ConstraintViolation<MenuItemRequest>> violations = validator.validate(request);

        assertFalse(violations.isEmpty());
        assertTrue(violations.stream().anyMatch(v -> v.getMessage().equals("Item name is required")));
    }

    @Test
    void validationFailsWhenPriceIsNull() {
        MenuItemRequest request = MenuItemRequest.builder()
                .name("Burger")
                .price(null)
                .category(MenuCategory.ENTREE)
                .build();

        Set<ConstraintViolation<MenuItemRequest>> violations = validator.validate(request);

        assertFalse(violations.isEmpty());
        assertTrue(violations.stream().anyMatch(v -> v.getMessage().equals("Price is required")));
    }

    @Test
    void validationFailsWhenPriceIsLessThanMinimum() {
        MenuItemRequest request = MenuItemRequest.builder()
                .name("Burger")
                .price(BigDecimal.valueOf(0.00))
                .category(MenuCategory.ENTREE)
                .build();

        Set<ConstraintViolation<MenuItemRequest>> violations = validator.validate(request);

        assertFalse(violations.isEmpty());
        assertTrue(violations.stream().anyMatch(v -> v.getMessage().equals("Price must be greater than 0")));
    }

    @Test
    void validationFailsWhenCategoryIsNull() {
        MenuItemRequest request = MenuItemRequest.builder()
                .name("Burger")
                .price(BigDecimal.valueOf(10.00))
                .category(null)
                .build();

        Set<ConstraintViolation<MenuItemRequest>> violations = validator.validate(request);

        assertFalse(violations.isEmpty());
        assertTrue(violations.stream().anyMatch(v -> v.getMessage().equals("Category is required")));
    }

    @Test
    void validationPassesWhenAllFieldsAreValid() {
        MenuItemRequest request = MenuItemRequest.builder()
                .name("Burger")
                .price(BigDecimal.valueOf(10.00))
                .category(MenuCategory.ENTREE)
                .build();

        Set<ConstraintViolation<MenuItemRequest>> violations = validator.validate(request);

        assertTrue(violations.isEmpty());
    }

}
