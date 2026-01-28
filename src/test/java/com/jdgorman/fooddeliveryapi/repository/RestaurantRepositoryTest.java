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
        Restaurant restaurant = new Restaurant();
        restaurant.setName("Test Restaurant");
        restaurant.setAddress("123 Test Street");
        restaurant.setCuisineType("Test Cuisine");
        restaurant.setPhone("123-456-7890");
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
        Restaurant restaurant = new Restaurant();
        restaurant.setName("Partial Match Restaurant");
        restaurant.setAddress("789 Partial Street");
        restaurant.setCuisineType("Partial Cuisine");
        restaurant.setPhone("987-654-3210");
        restaurantRepository.save(restaurant);

        boolean exists = restaurantRepository.existsByNameAndAddress("Partial Match Restaurant", "Wrong Address");

        assertFalse(exists);
    }
}
