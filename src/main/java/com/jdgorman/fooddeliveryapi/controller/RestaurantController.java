package com.jdgorman.fooddeliveryapi.controller;

import com.jdgorman.fooddeliveryapi.entity.Restaurant;
import com.jdgorman.fooddeliveryapi.service.RestaurantService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;

/**
 * REST controller that exposes CRUD endpoints for managing restaurants.
 * <p>
 * Base path: <code>/api/restaurants</code>
 * <p>
 * This controller delegates business logic to {@link RestaurantService} and accepts/returns
 * {@link Restaurant} entities. Requests that create or update restaurants are validated
 * using Bean Validation annotations on the entity.
 */
@RestController
@RequestMapping(value = "/api/restaurants", produces = "application/json")
@RequiredArgsConstructor
@Validated
public class RestaurantController {

    private final RestaurantService restaurantService;

    /**
     * Retrieve all restaurants.
     *
     * @return HTTP 200 OK with a list of {@link Restaurant} objects. If no restaurants exist,
     * an empty list is returned.
     */
    @GetMapping
    public ResponseEntity<List<Restaurant>> getAllRestaurants() {
        List<Restaurant> list = restaurantService.getAllRestaurants();
        return ResponseEntity.ok(list == null ? Collections.emptyList() : list);
    }

    /**
     * Retrieve a single restaurant by id.
     *
     * @param id the id of the restaurant to retrieve
     * @return HTTP 200 OK with the {@link Restaurant} when found
     */
    @GetMapping("/{id}")
    public ResponseEntity<Restaurant> getRestaurantById(@PathVariable Long id) {
        return ResponseEntity.ok(restaurantService.getRestaurantById(id));
    }

    /**
     * Create a new restaurant.
     *
     * The request body is validated; if validation fails, a 400 response is returned by the
     * global exception handler. If creation succeeds this returns HTTP 201 Created with the
     * created entity in the response body.
     *
     * @param restaurant the {@link Restaurant} payload to create; fields are validated
     * @return HTTP 201 Created with the created {@link Restaurant}
     */
    @PostMapping(consumes = "application/json")
    public ResponseEntity<Restaurant> createRestaurant(@Valid @RequestBody Restaurant restaurant) {
        Restaurant created = restaurantService.createRestaurant(restaurant);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    /**
     * Update an existing restaurant.
     *
     * The request body is validated. If the payload contains an id that does not match the
     * path id, a 400 Bad Request is returned. If the restaurant does not exist, a 404 Not Found
     * is returned. On success returns the updated resource.
     *
     * @param id the id of the restaurant to update
     * @param restaurant the {@link Restaurant} payload containing updated values; validated
     */
    @PutMapping(value = "/{id}", consumes = "application/json")
    public ResponseEntity<Restaurant> updateRestaurant(@PathVariable Long id, @Valid @RequestBody Restaurant restaurant) {
        if (restaurant.getId() != null && !restaurant.getId().equals(id)) {
            return ResponseEntity.badRequest().build();
        }
        Restaurant updated = restaurantService.updateRestaurant(id, restaurant);
        if (updated == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(updated);
    }

    /**
     * Delete a restaurant by id.
     *
     * @param id the id of the restaurant to delete
     * @return HTTP 204 No Content when delete succeeds
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteRestaurant(@PathVariable Long id) {
        restaurantService.deleteRestaurant(id);
        return ResponseEntity.noContent().build();
    }
}
