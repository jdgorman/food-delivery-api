package com.jdgorman.fooddeliveryapi.dto;

import com.jdgorman.fooddeliveryapi.enumerator.MenuCategory;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;

class MenuItemResponseTest {

    @Test
    void menuItemResponseBuilderCreatesObjectWithCorrectValues() {
        LocalDateTime now = LocalDateTime.now();

        MenuItemResponse response = MenuItemResponse.builder()
                .id(1L)
                .name("Spring Rolls")
                .description("Crispy rolls with vegetables")
                .price(BigDecimal.valueOf(5.99))
                .category(MenuCategory.APPETIZER)
                .isAvailable(true)
                .restaurantId(101L)
                .restaurantName("Golden Dragon")
                .createTimestamp(now)
                .updateTimestamp(now)
                .build();

        assertEquals(1L, response.getId());
        assertEquals("Spring Rolls", response.getName());
        assertEquals("Crispy rolls with vegetables", response.getDescription());
        assertEquals(BigDecimal.valueOf(5.99), response.getPrice());
        assertEquals(MenuCategory.APPETIZER, response.getCategory());
        assertEquals(true, response.getIsAvailable());
        assertEquals(101L, response.getRestaurantId());
        assertEquals("Golden Dragon", response.getRestaurantName());
        assertEquals(now, response.getCreateTimestamp());
        assertEquals(now, response.getUpdateTimestamp());
    }

    @Test
    void menuItemResponseBuilderHandlesNullValues() {
        MenuItemResponse response = MenuItemResponse.builder()
                .id(null)
                .name(null)
                .description(null)
                .price(null)
                .category(null)
                .isAvailable(null)
                .restaurantId(null)
                .restaurantName(null)
                .createTimestamp(null)
                .updateTimestamp(null)
                .build();

        assertEquals(null, response.getId());
        assertEquals(null, response.getName());
        assertEquals(null, response.getDescription());
        assertEquals(null, response.getPrice());
        assertEquals(null, response.getCategory());
        assertEquals(null, response.getIsAvailable());
        assertEquals(null, response.getRestaurantId());
        assertEquals(null, response.getRestaurantName());
        assertEquals(null, response.getCreateTimestamp());
        assertEquals(null, response.getUpdateTimestamp());
    }
}
