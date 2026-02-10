package com.jdgorman.fooddeliveryapi.entity;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class MenuItemTest {

    @Test
    void menuItemBuilderCreatesValidObject() {
        MenuItem menuItem = MenuItem.builder()
                .id(1L)
                .name("Spring Rolls")
                .description("Crispy rolls with vegetables")
                .price(BigDecimal.valueOf(5.99))
                .category(MenuCategory.APPETIZER)
                .isAvailable(true)
                .build();

        assertNotNull(menuItem);
        assertEquals(1L, menuItem.getId());
        assertEquals("Spring Rolls", menuItem.getName());
        assertEquals("Crispy rolls with vegetables", menuItem.getDescription());
        assertEquals(BigDecimal.valueOf(5.99), menuItem.getPrice());
        assertEquals(MenuCategory.APPETIZER, menuItem.getCategory());
        assertEquals(true, menuItem.getIsAvailable());
    }

    @Test
    void menuItemBuilderHandlesNullValues() {
        MenuItem menuItem = MenuItem.builder()
                .id(null)
                .name(null)
                .description(null)
                .price(null)
                .category(null)
                .isAvailable(null)
                .build();

        assertNotNull(menuItem);
        assertEquals(null, menuItem.getId());
        assertEquals(null, menuItem.getName());
        assertEquals(null, menuItem.getDescription());
        assertEquals(null, menuItem.getPrice());
        assertEquals(null, menuItem.getCategory());
        assertEquals(null, menuItem.getIsAvailable());
    }

    @Test
    void menuItemBuilderHandlesNegativePrice() {
        MenuItem menuItem = MenuItem.builder()
                .id(2L)
                .name("Negative Price Item")
                .description("This item has a negative price")
                .price(BigDecimal.valueOf(-1.99))
                .category(MenuCategory.ENTREE)
                .isAvailable(true)
                .build();

        assertNotNull(menuItem);
        assertEquals(BigDecimal.valueOf(-1.99), menuItem.getPrice());
    }
}
