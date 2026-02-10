package com.jdgorman.fooddeliveryapi.service;

import com.jdgorman.fooddeliveryapi.entity.Restaurant;
import com.jdgorman.fooddeliveryapi.exception.DuplicateResourceException;
import com.jdgorman.fooddeliveryapi.exception.ResourceNotFoundException;
import com.jdgorman.fooddeliveryapi.repository.RestaurantRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Service layer for managing restaurants.
 *
 * <p>This service encapsulates business rules and repository interactions for CRUD operations
 * on {@link Restaurant} entities. It throws {@link ResourceNotFoundException} when a requested
 * entity does not exist and {@link DuplicateResourceException} when a create operation would
 * violate uniqueness constraints.</p>
 */
@Service
@RequiredArgsConstructor
public class RestaurantService {

    private final RestaurantRepository restaurantRepository;

    /**
     * Retrieve all restaurants.
     *
     * @return a list of all {@link Restaurant} entities; may be empty but never null
     */
    public List<Restaurant> getAllRestaurants() {
        return restaurantRepository.findAll();
    }

    /**
     * Retrieve a specific restaurant by id.
     *
     * @param id the id of the restaurant to retrieve
     * @return the found {@link Restaurant}
     * @throws ResourceNotFoundException if no restaurant with the provided id exists
     */
    public Restaurant getRestaurantById(Long id) {
        return restaurantRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Restaurant with id " + id + " does not exist"
                ));
    }

    /**
     * Create a new restaurant.
     *
     * <p>This method checks for an existing restaurant with the same name and address and
     * throws {@link DuplicateResourceException} to prevent duplicates. On success it persists
     * and returns the saved entity.</p>
     *
     * @param restaurant the {@link Restaurant} entity to create; must contain the required fields
     * @return the persisted {@link Restaurant}
     * @throws DuplicateResourceException if a restaurant with the same name and address already exists
     */
    public Restaurant createRestaurant(Restaurant restaurant) {
        if (restaurantRepository.existsByNameAndAddress(
                restaurant.getName(),
                restaurant.getAddress())) {
            throw new DuplicateResourceException(
                    "Restaurant with name '" + restaurant.getName() +
                            "' at address '" + restaurant.getAddress() + "' already exists"
            );
        }
        return restaurantRepository.save(restaurant);
    }

    /**
     * Update an existing restaurant.
     *
     * <p>Validates that the restaurant exists, applies the provided updates to the existing
     * entity, sets the update timestamp, and persists the change.</p>
     *
     * @param id         the id of the restaurant to update
     * @param restaurant the {@link Restaurant} entity carrying updated values
     * @return the updated {@link Restaurant}
     * @throws ResourceNotFoundException if no restaurant with the provided id exists
     */
    public Restaurant updateRestaurant(Long id, Restaurant restaurant) {
        Restaurant existing = restaurantRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Cannot update. Restaurant with id " + id + " does not exist"
                ));
        existing.setName(restaurant.getName());
        existing.setAddress(restaurant.getAddress());
        existing.setPhone(restaurant.getPhone());
        existing.setCuisineType(restaurant.getCuisineType());
        existing.setActive(restaurant.getActive());
        existing.setUpdateTimestamp(LocalDateTime.now());
        return restaurantRepository.save(existing);
    }

    /**
     * Delete a restaurant by id.
     *
     * @param id the id of the restaurant to delete
     * @throws ResourceNotFoundException if no restaurant with the provided id exists
     */
    public void deleteRestaurant(Long id) {
        if (!restaurantRepository.existsById(id)) {
            throw new ResourceNotFoundException(
                    "Cannot delete. Restaurant with id " + id + " does not exist"
            );
        }
        restaurantRepository.deleteById(id);
    }
}