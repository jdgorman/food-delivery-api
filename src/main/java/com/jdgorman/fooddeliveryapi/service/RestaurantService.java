package com.jdgorman.fooddeliveryapi.service;

import com.jdgorman.fooddeliveryapi.entity.Restaurant;
import com.jdgorman.fooddeliveryapi.exception.DuplicateResourceException;
import com.jdgorman.fooddeliveryapi.exception.ResourceNotFoundException;
import com.jdgorman.fooddeliveryapi.repository.RestaurantRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
@RequiredArgsConstructor
public class RestaurantService {

    private final RestaurantRepository restaurantRepository;

    public List<Restaurant> getAllRestaurants() {
        return restaurantRepository.findAll();
    }

    public Restaurant getRestaurantById(Long id) {
        return restaurantRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Restaurant with id " + id + " does not exist"
                ));
    }

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
        return restaurantRepository.save(existing);
    }

    public void deleteRestaurant(Long id) {
        if (!restaurantRepository.existsById(id)) {
            throw new ResourceNotFoundException(
                    "Cannot delete. Restaurant with id " + id + " does not exist"
            );
        }
        restaurantRepository.deleteById(id);
    }
}