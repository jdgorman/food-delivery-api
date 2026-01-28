package com.jdgorman.fooddeliveryapi.controller;

import com.jdgorman.fooddeliveryapi.entity.Restaurant;
import com.jdgorman.fooddeliveryapi.service.RestaurantService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class RestaurantControllerTest {

    private final RestaurantService restaurantService = mock(RestaurantService.class);
    private final RestaurantController restaurantController = new RestaurantController(restaurantService);

    @Test
    void getAllRestaurantsReturnsEmptyListWhenNoRestaurantsExist() {
        when(restaurantService.getAllRestaurants()).thenReturn(Collections.emptyList());

        ResponseEntity<List<Restaurant>> response = restaurantController.getAllRestaurants();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(0, response.getBody().size());
        verify(restaurantService, times(1)).getAllRestaurants();
    }

    @Test
    void getAllRestaurantsReturnsListOfRestaurants() {
        Restaurant restaurant = new Restaurant();
        restaurant.setId(1L);
        restaurant.setName("Test Restaurant");
        when(restaurantService.getAllRestaurants()).thenReturn(List.of(restaurant));

        ResponseEntity<List<Restaurant>> response = restaurantController.getAllRestaurants();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1, response.getBody().size());
        assertEquals("Test Restaurant", response.getBody().get(0).getName());
        verify(restaurantService, times(1)).getAllRestaurants();
    }

    @Test
    void getRestaurantByIdReturnsRestaurantWhenFound() {
        Restaurant restaurant = new Restaurant();
        restaurant.setId(1L);
        restaurant.setName("Test Restaurant");
        when(restaurantService.getRestaurantById(1L)).thenReturn(restaurant);

        ResponseEntity<Restaurant> response = restaurantController.getRestaurantById(1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Test Restaurant", response.getBody().getName());
        verify(restaurantService, times(1)).getRestaurantById(1L);
    }

    @Test
    void getRestaurantByIdReturnsNotFoundWhenRestaurantDoesNotExist() {
        when(restaurantService.getRestaurantById(1L)).thenReturn(null);

        ResponseEntity<Restaurant> response = restaurantController.getRestaurantById(1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(null, response.getBody());
        verify(restaurantService, times(1)).getRestaurantById(1L);
    }

    @Test
    void createRestaurantReturnsCreatedRestaurant() {
        Restaurant restaurant = new Restaurant();
        restaurant.setName("New Restaurant");
        when(restaurantService.createRestaurant(Mockito.any(Restaurant.class))).thenReturn(restaurant);

        ResponseEntity<Restaurant> response = restaurantController.createRestaurant(restaurant);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals("New Restaurant", response.getBody().getName());
        verify(restaurantService, times(1)).createRestaurant(Mockito.any(Restaurant.class));
    }

    @Test
    void updateRestaurantReturnsUpdatedRestaurantWhenSuccessful() {
        Restaurant restaurant = new Restaurant();
        restaurant.setId(1L);
        restaurant.setName("Updated Restaurant");
        when(restaurantService.updateRestaurant(eq(1L), any(Restaurant.class))).thenReturn(restaurant);

        ResponseEntity<Restaurant> response = restaurantController.updateRestaurant(1L, restaurant);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Updated Restaurant", response.getBody().getName());
        verify(restaurantService, times(1)).updateRestaurant(eq(1L), any(Restaurant.class));
    }

    @Test
    void updateRestaurantReturnsBadRequestWhenIdMismatch() {
        Restaurant restaurant = new Restaurant();
        restaurant.setId(2L);

        ResponseEntity<Restaurant> response = restaurantController.updateRestaurant(1L, restaurant);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        verify(restaurantService, never()).updateRestaurant(anyLong(), any(Restaurant.class));
    }

    @Test
    void updateRestaurantReturnsNotFoundWhenRestaurantDoesNotExist() {
        when(restaurantService.updateRestaurant(eq(1L), any(Restaurant.class))).thenReturn(null);

        ResponseEntity<Restaurant> response = restaurantController.updateRestaurant(1L, new Restaurant());

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        verify(restaurantService, times(1)).updateRestaurant(eq(1L), any(Restaurant.class));
    }

    @Test
    void deleteRestaurantReturnsNoContentWhenSuccessful() {
        doNothing().when(restaurantService).deleteRestaurant(1L);

        ResponseEntity<Void> response = restaurantController.deleteRestaurant(1L);

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        verify(restaurantService, times(1)).deleteRestaurant(1L);
    }
}
