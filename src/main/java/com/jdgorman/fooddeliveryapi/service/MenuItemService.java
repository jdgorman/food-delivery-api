package com.jdgorman.fooddeliveryapi.service;

import com.jdgorman.fooddeliveryapi.dto.MenuItemRequest;
import com.jdgorman.fooddeliveryapi.dto.MenuItemResponse;
import com.jdgorman.fooddeliveryapi.enumerator.MenuCategory;
import com.jdgorman.fooddeliveryapi.entity.MenuItem;
import com.jdgorman.fooddeliveryapi.entity.Restaurant;
import com.jdgorman.fooddeliveryapi.exception.ResourceNotFoundException;
import com.jdgorman.fooddeliveryapi.repository.MenuItemRepository;
import com.jdgorman.fooddeliveryapi.repository.RestaurantRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Service responsible for menu item business logic.
 * <p>
 * Provides methods to create, read, update and delete menu items belonging to restaurants.
 * All returned objects are {@link MenuItemResponse} DTOs; entities are managed internally.
 */
@Service
@RequiredArgsConstructor
public class MenuItemService {

    private final MenuItemRepository menuItemRepository;
    private final RestaurantRepository restaurantRepository;

    /**
     * Retrieve all menu items for a specific restaurant.
     *
     * @param restaurantId id of the restaurant whose menu items are requested
     * @return a list of {@link MenuItemResponse} DTOs representing the restaurant's menu items; never null
     */
    public List<MenuItemResponse> getMenuItemsByRestaurant(Long restaurantId) {
        // Verify restaurant exists
        restaurantRepository.findById(restaurantId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Restaurant with id " + restaurantId + " does not exist"
                ));

        List<MenuItem> menuItems = menuItemRepository.findByRestaurantId(restaurantId);
        return menuItems.stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    /**
     * Retrieve a specific menu item by id and ensure it belongs to the provided restaurant.
     *
     * @param restaurantId id of the restaurant which should own the menu item
     * @param id           id of the menu item to retrieve
     * @return a {@link MenuItemResponse} representing the requested menu item
     */
    public MenuItemResponse getMenuItemById(Long restaurantId, Long id) {
        MenuItem menuItem = menuItemRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Menu item with id " + id + " does not exist"
                ));

        // Validate that menu item belongs to the specified restaurant
        if (!menuItem.getRestaurant().getId().equals(restaurantId)) {
            throw new ResourceNotFoundException(
                    "Menu item with id " + id + " does not belong to restaurant " + restaurantId
            );
        }

        return convertToResponse(menuItem);
    }

    /**
     * Retrieve menu items for a restaurant filtered by category.
     *
     * @param restaurantId id of the restaurant whose menu items are requested
     * @param category     category to filter by (required)
     * @return a list of {@link MenuItemResponse} matching the provided category; never null
     */
    public List<MenuItemResponse> getMenuItemsByRestaurantAndCategory(Long restaurantId, MenuCategory category) {
        // Verify restaurant exists
        restaurantRepository.findById(restaurantId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Restaurant with id " + restaurantId + " does not exist"
                ));

        List<MenuItem> menuItems = menuItemRepository.findByRestaurantIdAndCategory(restaurantId, category);
        return menuItems.stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    /**
     * Create a new menu item for a restaurant.
     *
     *
     * @param restaurantId id of the restaurant to create the menu item for
     * @param request      DTO containing menu item properties (name, description, price, category, availability)
     * @return {@link MenuItemResponse} representing the created menu item
     */
    @Transactional
    public MenuItemResponse createMenuItem(Long restaurantId, MenuItemRequest request) {
        Restaurant restaurant = restaurantRepository.findById(restaurantId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Restaurant with id " + restaurantId + " does not exist"
                ));

        MenuItem menuItem = MenuItem.builder()
                .name(request.getName())
                .description(request.getDescription())
                .price(request.getPrice())
                .category(request.getCategory())
                .isAvailable(request.getIsAvailable() != null ? request.getIsAvailable() : true)
                .restaurant(restaurant)
                .build();

        MenuItem saved = menuItemRepository.save(menuItem);
        return convertToResponse(saved);
    }

    /**
     * Update an existing menu item for a restaurant.
     *
     * @param restaurantId id of the restaurant that should own the menu item
     * @param id           id of the menu item to update
     * @param request      DTO containing updated properties
     * @return {@link MenuItemResponse} representing the updated menu item
     */
    @Transactional
    public MenuItemResponse updateMenuItem(Long restaurantId, Long id, MenuItemRequest request) {
        MenuItem menuItem = menuItemRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Cannot update. Menu item with id " + id + " does not exist"
                ));

        // Validate that menu item belongs to the specified restaurant
        if (!menuItem.getRestaurant().getId().equals(restaurantId)) {
            throw new ResourceNotFoundException(
                    "Menu item with id " + id + " does not belong to restaurant " + restaurantId
            );
        }

        menuItem.setName(request.getName());
        menuItem.setDescription(request.getDescription());
        menuItem.setPrice(request.getPrice());
        menuItem.setCategory(request.getCategory());
        menuItem.setIsAvailable(
                request.getIsAvailable() != null ? request.getIsAvailable() : menuItem.getIsAvailable()
        );
        menuItem.setUpdateTimestamp(LocalDateTime.now());

        MenuItem updated = menuItemRepository.save(menuItem);
        return convertToResponse(updated);
    }

    /**
     * Delete a menu item by id for a restaurant after validating ownership.
     *
     * @param restaurantId id of the restaurant that should own the menu item
     * @param id           id of the menu item to delete
     */
    @Transactional
    public void deleteMenuItem(Long restaurantId, Long id) {
        MenuItem menuItem = menuItemRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Cannot delete. Menu item with id " + id + " does not exist"
                ));

        // Validate that menu item belongs to the specified restaurant
        if (!menuItem.getRestaurant().getId().equals(restaurantId)) {
            throw new ResourceNotFoundException(
                    "Menu item with id " + id + " does not belong to restaurant " + restaurantId
            );
        }

        menuItemRepository.deleteById(id);
    }

    /**
     * Convert MenuItem entity to MenuItemResponse DTO.
     *
     * @param menuItem the entity to convert
     * @return a populated {@link MenuItemResponse}
     */
    private MenuItemResponse convertToResponse(MenuItem menuItem) {
        return MenuItemResponse.builder()
                .id(menuItem.getId())
                .name(menuItem.getName())
                .description(menuItem.getDescription())
                .price(menuItem.getPrice())
                .category(menuItem.getCategory())
                .isAvailable(menuItem.getIsAvailable())
                .restaurantId(menuItem.getRestaurant().getId())
                .restaurantName(menuItem.getRestaurant().getName())
                .createTimestamp(menuItem.getCreateTimestamp())
                .updateTimestamp(menuItem.getUpdateTimestamp())
                .build();
    }
}