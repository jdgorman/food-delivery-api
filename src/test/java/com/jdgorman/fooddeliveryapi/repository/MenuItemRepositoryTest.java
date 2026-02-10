package com.jdgorman.fooddeliveryapi.repository;

import com.jdgorman.fooddeliveryapi.entity.MenuCategory;
import com.jdgorman.fooddeliveryapi.entity.MenuItem;
import com.jdgorman.fooddeliveryapi.entity.Restaurant;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;


import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DataJpaTest
class MenuItemRepositoryTest {

    @Autowired
    private MenuItemRepository menuItemRepository;

    @Autowired
    private RestaurantRepository restaurantRepository;

    private Restaurant restaurant;

    @BeforeEach
    void setUp() {
        restaurant = Restaurant.builder()
                .name("Test Restaurant")
                .address("123 Test St")
                .phone("555-123-4567")
                .cuisineType("Test Cuisine")
                .active(true)
                .build();
        restaurant = restaurantRepository.save(restaurant);
    }

    @Test
    void findByRestaurantIdReturnsMenuItemsForSpecificRestaurant() {
        MenuItem item1 = MenuItem.builder()
                .name("Item 1")
                .price(BigDecimal.TEN)
                .restaurant(restaurant)
                .category(MenuCategory.ENTREE)
                .isAvailable(true)
                .build();
        MenuItem item2 = MenuItem.builder()
                .name("Item 2")
                .price(BigDecimal.valueOf(15.99))
                .restaurant(restaurant)
                .category(MenuCategory.ENTREE)
                .isAvailable(true)
                .build();
        menuItemRepository.save(item1);
        menuItemRepository.save(item2);

        List<MenuItem> items = menuItemRepository.findByRestaurantId(restaurant.getId());

        assertEquals(2, items.size());
    }

    @Test
    void findByRestaurantIdAndCategoryReturnsMenuItemsForSpecificCategory() {
        MenuItem item1 = MenuItem.builder()
                .name("Spring Rolls")
                .price(BigDecimal.valueOf(5.99))
                .restaurant(restaurant)
                .category(MenuCategory.APPETIZER)
                .isAvailable(true)
                .build();
        MenuItem item2 = MenuItem.builder()
                .name("Dumplings")
                .price(BigDecimal.valueOf(6.99))
                .restaurant(restaurant)
                .category(MenuCategory.APPETIZER)
                .isAvailable(true)
                .build();
        menuItemRepository.save(item1);
        menuItemRepository.save(item2);

        List<MenuItem> items = menuItemRepository.findByRestaurantIdAndCategory(restaurant.getId(), MenuCategory.APPETIZER);

        assertEquals(2, items.size());
    }
}
