package com.jdgorman.fooddeliveryapi.entity;

import com.jdgorman.fooddeliveryapi.enumerator.MenuCategory;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNotNull;

class MenuCategoryTest {

    @Test
    void enumContainsAppetizer() {
        assertNotNull(MenuCategory.valueOf("APPETIZER"));
    }

    @Test
    void enumContainsEntree() {
        assertNotNull(MenuCategory.valueOf("ENTREE"));
    }

    @Test
    void enumContainsDessert() {
        assertNotNull(MenuCategory.valueOf("DESSERT"));
    }

    @Test
    void enumContainsBeverage() {
        assertNotNull(MenuCategory.valueOf("BEVERAGE"));
    }

    @Test
    void enumContainsSide() {
        assertNotNull(MenuCategory.valueOf("SIDE"));
    }

    @Test
    void enumContainsSpecial() {
        assertNotNull(MenuCategory.valueOf("SPECIAL"));
    }
}
