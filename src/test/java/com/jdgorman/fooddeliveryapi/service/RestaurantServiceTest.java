package com.jdgorman.fooddeliveryapi.service;

import com.jdgorman.fooddeliveryapi.entity.Restaurant;
import com.jdgorman.fooddeliveryapi.exception.DuplicateResourceException;
import com.jdgorman.fooddeliveryapi.exception.ResourceNotFoundException;
import com.jdgorman.fooddeliveryapi.repository.RestaurantRepository;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class RestaurantServiceTest {

    @Mock
    private RestaurantRepository restaurantRepository;

    @InjectMocks
    private RestaurantService restaurantService;

    public RestaurantServiceTest() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getAllRestaurantsReturnsListOfRestaurants() {
        when(restaurantRepository.findAll()).thenReturn(List.of(new Restaurant(), new Restaurant()));

        List<Restaurant> restaurants = restaurantService.getAllRestaurants();

        assertEquals(2, restaurants.size());
    }

    @Test
    void getRestaurantByIdReturnsRestaurantWhenFound() {
        Restaurant restaurant = new Restaurant();
        restaurant.setId(1L);
        when(restaurantRepository.findById(1L)).thenReturn(Optional.of(restaurant));

        Restaurant result = restaurantService.getRestaurantById(1L);

        assertEquals(1L, result.getId());
    }

    @Test
    void getRestaurantByIdThrowsExceptionWhenNotFound() {
        when(restaurantRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> restaurantService.getRestaurantById(1L));
    }

    @Test
    void createRestaurantSavesAndReturnsRestaurantWhenNotDuplicate() {
        Restaurant restaurant = new Restaurant();
        restaurant.setName("Test Restaurant");
        restaurant.setAddress("123 Test Street");
        when(restaurantRepository.existsByNameAndAddress("Test Restaurant", "123 Test Street")).thenReturn(false);
        when(restaurantRepository.save(restaurant)).thenReturn(restaurant);

        Restaurant result = restaurantService.createRestaurant(restaurant);

        assertEquals("Test Restaurant", result.getName());
        assertEquals("123 Test Street", result.getAddress());
    }

    @Test
    void createRestaurantThrowsExceptionWhenDuplicate() {
        Restaurant restaurant = new Restaurant();
        restaurant.setName("Test Restaurant");
        restaurant.setAddress("123 Test Street");
        when(restaurantRepository.existsByNameAndAddress("Test Restaurant", "123 Test Street")).thenReturn(true);

        assertThrows(DuplicateResourceException.class, () -> restaurantService.createRestaurant(restaurant));
    }

    @Test
    void updateRestaurantUpdatesAndReturnsRestaurantWhenFound() {
        Restaurant existing = new Restaurant();
        existing.setId(1L);
        Restaurant updated = new Restaurant();
        updated.setName("Updated Name");
        updated.setAddress("Updated Address");
        when(restaurantRepository.findById(1L)).thenReturn(Optional.of(existing));
        when(restaurantRepository.save(existing)).thenReturn(existing);

        Restaurant result = restaurantService.updateRestaurant(1L, updated);

        assertEquals("Updated Name", result.getName());
        assertEquals("Updated Address", result.getAddress());
    }

    @Test
    void updateRestaurantThrowsExceptionWhenNotFound() {
        Restaurant updated = new Restaurant();
        when(restaurantRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> restaurantService.updateRestaurant(1L, updated));
    }

    @Test
    void deleteRestaurantDeletesWhenFound() {
        when(restaurantRepository.existsById(1L)).thenReturn(true);

        restaurantService.deleteRestaurant(1L);

        verify(restaurantRepository).deleteById(1L);
    }

    @Test
    void deleteRestaurantThrowsExceptionWhenNotFound() {
        when(restaurantRepository.existsById(1L)).thenReturn(false);

        assertThrows(ResourceNotFoundException.class, () -> restaurantService.deleteRestaurant(1L));
    }
}
