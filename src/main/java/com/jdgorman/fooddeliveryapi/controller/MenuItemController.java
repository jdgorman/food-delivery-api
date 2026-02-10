package com.jdgorman.fooddeliveryapi.controller;

import com.jdgorman.fooddeliveryapi.dto.MenuItemRequest;
import com.jdgorman.fooddeliveryapi.dto.MenuItemResponse;
import com.jdgorman.fooddeliveryapi.entity.MenuCategory;
import com.jdgorman.fooddeliveryapi.service.MenuItemService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST controller that manages menu items for restaurants.
 * <p>
 * Endpoints are mounted under the <code>/api</code> base path. This controller delegates
 * business logic to {@link MenuItemService} and accepts/returns DTOs: {@link MenuItemRequest}
 * and {@link MenuItemResponse}.
 */
@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class MenuItemController {

    private final MenuItemService menuItemService;

    /**
     * Retrieve all menu items for a restaurant. If a {@code category} query parameter is provided,
     * the results are filtered to only include items from that category.
     *
     * @param restaurantId the id of the restaurant whose menu items should be returned
     * @param category     optional menu category to filter by (may be {@code null})
     * @return HTTP 200 OK with a list of {@link MenuItemResponse} objects; list may be empty
     * @throws com.jdgorman.fooddeliveryapi.exception.ResourceNotFoundException if the restaurant does not exist
     */
    @GetMapping("/restaurants/{restaurantId}/menu-items")
    public ResponseEntity<List<MenuItemResponse>> getMenuItemsByRestaurant(
            @PathVariable Long restaurantId,
            @RequestParam(required = false) MenuCategory category) {

        if (category != null) {
            List<MenuItemResponse> menuItems = menuItemService.getMenuItemsByRestaurantAndCategory(restaurantId, category);
            return ResponseEntity.ok(menuItems);
        }

        List<MenuItemResponse> menuItems = menuItemService.getMenuItemsByRestaurant(restaurantId);
        return ResponseEntity.ok(menuItems);
    }

    /**
     * Retrieve a specific menu item by id for the given restaurant.
     *
     * @param restaurantId the id of the restaurant that owns the menu item
     * @param id           the id of the menu item to retrieve
     * @return HTTP 200 OK with the {@link MenuItemResponse}
     * @throws com.jdgorman.fooddeliveryapi.exception.ResourceNotFoundException if the menu item does not exist
     *         or does not belong to the given restaurant
     */
    @GetMapping("/restaurants/{restaurantId}/menu-items/{id}")
    public ResponseEntity<MenuItemResponse> getMenuItemById(
            @PathVariable Long restaurantId,
            @PathVariable Long id) {
        MenuItemResponse menuItem = menuItemService.getMenuItemById(restaurantId, id);
        return ResponseEntity.ok(menuItem);
    }

    /**
     * Create a new menu item for the specified restaurant.
     *
     * @param restaurantId the id of the restaurant to add the menu item to
     * @param request      the {@link MenuItemRequest} payload containing name, description, price,
     *                     category and availability; validated using Bean Validation
     * @return HTTP 201 Created with the created {@link MenuItemResponse}
     * @throws com.jdgorman.fooddeliveryapi.exception.ResourceNotFoundException if the restaurant does not exist
     * @throws jakarta.validation.ConstraintViolationException if the request payload fails validation
     */
    @PostMapping("/restaurants/{restaurantId}/menu-items")
    public ResponseEntity<MenuItemResponse> createMenuItem(
            @PathVariable Long restaurantId,
            @Valid @RequestBody MenuItemRequest request) {

        try {
            MenuItemResponse created = menuItemService.createMenuItem(restaurantId, request);
            return ResponseEntity.status(HttpStatus.CREATED).body(created);
        } catch (Exception e) {
            System.out.println("Error creating menu item: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    /**
     * Update an existing menu item for a restaurant.
     *
     * @param restaurantId the id of the restaurant that owns the menu item
     * @param id           the id of the menu item to update
     * @param request      the {@link MenuItemRequest} payload with updated values; validated using Bean Validation
     * @return HTTP 200 OK with the updated {@link MenuItemResponse}
     * @throws com.jdgorman.fooddeliveryapi.exception.ResourceNotFoundException if the menu item does not exist
     *         or does not belong to the given restaurant
     */
    @PutMapping("/restaurants/{restaurantId}/menu-items/{id}")
    public ResponseEntity<MenuItemResponse> updateMenuItem(
            @PathVariable Long restaurantId,
            @PathVariable Long id,
            @Valid @RequestBody MenuItemRequest request) {
        MenuItemResponse updated = menuItemService.updateMenuItem(restaurantId, id, request);
        return ResponseEntity.ok(updated);
    }

    /**
     * Delete a menu item from a restaurant.
     *
     * @param restaurantId the id of the restaurant that owns the menu item
     * @param id           the id of the menu item to delete
     * @return HTTP 204 No Content when deletion succeeds
     * @throws com.jdgorman.fooddeliveryapi.exception.ResourceNotFoundException if the menu item does not exist
     *         or does not belong to the given restaurant
     */
    @DeleteMapping("/restaurants/{restaurantId}/menu-items/{id}")
    public ResponseEntity<Void> deleteMenuItem(
            @PathVariable Long restaurantId,
            @PathVariable Long id) {
        menuItemService.deleteMenuItem(restaurantId, id);
        return ResponseEntity.noContent().build();
    }
}