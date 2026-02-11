package com.jdgorman.fooddeliveryapi.repository;

import com.jdgorman.fooddeliveryapi.entity.Restaurant;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DataJpaTest
class RestaurantRepositoryTest {

    @Autowired
    private RestaurantRepository restaurantRepository;

    @Test
    void existsByNameAndAddressReturnsTrueForExistingRestaurant() {
        Restaurant restaurant = Restaurant.builder()
                .name("Test Restaurant")
                .address("123 Test Street")
                .cuisineType("Test Cuisine")
                .phone("123-456-7890")
                .build();
        restaurantRepository.save(restaurant);

        boolean exists = restaurantRepository.existsByNameAndAddress("Test Restaurant", "123 Test Street");

        assertTrue(exists);
    }

    @Test
    void existsByNameAndAddressReturnsFalseForNonExistingRestaurant() {
        boolean exists = restaurantRepository.existsByNameAndAddress("Nonexistent Restaurant", "456 Fake Street");

        assertFalse(exists);
    }

    @Test
    void existsByNameAndAddressReturnsFalseForNullValues() {
        boolean exists = restaurantRepository.existsByNameAndAddress(null, null);

        assertFalse(exists);
    }

    @Test
    void existsByNameAndAddressReturnsFalseForPartialMatch() {
        Restaurant restaurant = Restaurant.builder()
                .name("Partial Match Restaurant")
                .address("789 Partial Street")
                .cuisineType("Partial Cuisine")
                .phone("987-654-3210")
                .build();
        restaurantRepository.save(restaurant);

        boolean exists = restaurantRepository.existsByNameAndAddress("Partial Match Restaurant", "Wrong Address");

        assertFalse(exists);
    }
}
