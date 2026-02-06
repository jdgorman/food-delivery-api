package com.jdgorman.fooddeliveryapi.service;

import com.jdgorman.fooddeliveryapi.dto.MenuItemRequest;
import com.jdgorman.fooddeliveryapi.dto.MenuItemResponse;
import com.jdgorman.fooddeliveryapi.entity.MenuCategory;
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

@Service
@RequiredArgsConstructor
public class MenuItemService {

    private final MenuItemRepository menuItemRepository;
    private final RestaurantRepository restaurantRepository;

    /**
     * Get all menu items for a specific restaurant
     * @param restaurantId
     * @return List<MenuItemResponse>
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
     * Get a specific menu item by ID for a restaurant
     * @param restaurantId
     * @param id
     * @return MenuItemResponse
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
     * Get menu items for a restaurant filtered by category
     * @param restaurantId
     * @param category
     * @return List<MenuItemResponse>
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
     * Create a new menu item for a restaurant
     * @param restaurantId
     * @param request MenuItemRequest containing menu item details
     * @return MenuItemResponse
     */
    @Transactional
    public MenuItemResponse createMenuItem(Long restaurantId, MenuItemRequest request) {
        Restaurant restaurant = restaurantRepository.findById(restaurantId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Restaurant with id " + restaurantId + " does not exist"
                ));

        MenuItem menuItem = new MenuItem();
        menuItem.setName(request.getName());
        menuItem.setDescription(request.getDescription());
        menuItem.setPrice(request.getPrice());
        menuItem.setCategory(request.getCategory());
        menuItem.setIsAvailable(request.getIsAvailable());
        menuItem.setRestaurant(restaurant);
        LocalDateTime now = LocalDateTime.now();
        menuItem.setCreateTimestamp(now);
        menuItem.setUpdateTimestamp(now);

        MenuItem saved = menuItemRepository.save(menuItem);
        return convertToResponse(saved);
    }

    /**
     * Update an existing menu item for a restaurant
     * @param restaurantId
     * @param id
     * @param request MenuItemRequest containing updated fields
     * @return MenuItemResponse
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
        menuItem.setIsAvailable(request.getIsAvailable());
        menuItem.setUpdateTimestamp(LocalDateTime.now());

        MenuItem updated = menuItemRepository.save(menuItem);
        return convertToResponse(updated);
    }

    /**
     * Delete a menu item by ID for a restaurant
     * @param restaurantId
     * @param id
     */
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
     * Convert MenuItem entity to MenuItemResponse DTO
     * @param menuItem
     * @return MenuItemResponse
     */
    private MenuItemResponse convertToResponse(MenuItem menuItem) {
        MenuItemResponse response = new MenuItemResponse();
        response.setId(menuItem.getId());
        response.setName(menuItem.getName());
        response.setDescription(menuItem.getDescription());
        response.setPrice(menuItem.getPrice());
        response.setCategory(menuItem.getCategory());
        response.setIsAvailable(menuItem.getIsAvailable());
        response.setRestaurantId(menuItem.getRestaurant().getId());
        response.setRestaurantName(menuItem.getRestaurant().getName());
        response.setCreateTimestamp(menuItem.getCreateTimestamp());
        response.setUpdateTimestamp(menuItem.getUpdateTimestamp());
        return response;
    }
}