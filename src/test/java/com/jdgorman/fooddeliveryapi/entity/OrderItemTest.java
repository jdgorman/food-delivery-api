package com.jdgorman.fooddeliveryapi.entity;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class OrderItemTest {

    private static final ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
    private final Validator validator = factory.getValidator();

    @AfterAll
    static void closeFactory() {
        if (factory != null) {
            factory.close();
        }
    }

    @Test
    @DisplayName("should build OrderItem with all fields set")
    void shouldBuildOrderItemWithAllFieldsSet() {
        CustomerOrder order = CustomerOrder.builder().id(1L).build();
        MenuItem menuItem = MenuItem.builder().id(1L).build();

        OrderItem orderItem = OrderItem.builder()
                .id(1L)
                .order(order)
                .menuItem(menuItem)
                .quantity(2)
                .priceAtOrder(BigDecimal.valueOf(10.00))
                .subtotal(BigDecimal.valueOf(20.00))
                .build();

        assertNotNull(orderItem);
        assertEquals(1L, orderItem.getId());
        assertEquals(order, orderItem.getOrder());
        assertEquals(menuItem, orderItem.getMenuItem());
        assertEquals(2, orderItem.getQuantity());
        assertEquals(BigDecimal.valueOf(10.00), orderItem.getPriceAtOrder());
        assertEquals(BigDecimal.valueOf(20.00), orderItem.getSubtotal());
    }

    @Test
    @DisplayName("should calculate subtotal correctly based on quantity and price")
    void shouldCalculateSubtotalCorrectly() {
        MenuItem menuItem = MenuItem.builder().id(1L).build();

        OrderItem orderItem = OrderItem.builder()
                .menuItem(menuItem)
                .quantity(3)
                .priceAtOrder(BigDecimal.valueOf(15.00))
                .build();

        BigDecimal expectedSubtotal = BigDecimal.valueOf(45.00);
        orderItem.setSubtotal(orderItem.getPriceAtOrder().multiply(BigDecimal.valueOf(orderItem.getQuantity())));

        assertEquals(expectedSubtotal, orderItem.getSubtotal());
    }

    @Test
    @DisplayName("should throw exception when quantity is less than 1")
    void shouldThrowExceptionWhenQuantityIsLessThanOne() {
        CustomerOrder order = CustomerOrder.builder().id(1L).build();
        MenuItem menuItem = MenuItem.builder().id(1L).build();

        OrderItem orderItem = OrderItem.builder()
                .order(order)
                .menuItem(menuItem)
                .quantity(0)
                .priceAtOrder(BigDecimal.ONE)
                .subtotal(BigDecimal.ONE)
                .build();

        Set<ConstraintViolation<OrderItem>> violations = validator.validate(orderItem);

        assertFalse(violations.isEmpty());
        assertTrue(violations.stream().anyMatch(v -> v.getMessage().equals("Quantity must be at least 1")));
    }
}
