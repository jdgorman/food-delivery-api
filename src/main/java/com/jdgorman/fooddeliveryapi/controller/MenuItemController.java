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

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class MenuItemController {

    private final MenuItemService menuItemService;

    /**
     * Get all menu items for a restaurant, optionally filtered by category
     * @param restaurantId
     * @param category
     * @return ResponseEntity<List<MenuItemResponse>>
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
     * Get a specific menu item by ID for a restaurant
     * @param restaurantId
     * @param id
     * @return ResponseEntity<MenuItemResponse>
     */
    @GetMapping("/restaurants/{restaurantId}/menu-items/{id}")
    public ResponseEntity<MenuItemResponse> getMenuItemById(
            @PathVariable Long restaurantId,
            @PathVariable Long id) {
        MenuItemResponse menuItem = menuItemService.getMenuItemById(restaurantId, id);
        return ResponseEntity.ok(menuItem);
    }

    /**
     * Create a new menu item for a restaurant
     * @param restaurantId
     * @param request MenuItemRequest containing name, description, price, category, and availability
     * @return ResponseEntity<MenuItemResponse>
     */
    @PostMapping("/restaurants/{restaurantId}/menu-items")
    public ResponseEntity<MenuItemResponse> createMenuItem(
            @PathVariable Long restaurantId,
            @Valid @RequestBody MenuItemRequest request) {
        MenuItemResponse created = menuItemService.createMenuItem(restaurantId, request);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    /**
     * Update an existing menu item for a restaurant
     * @param restaurantId
     * @param id
     * @param request MenuItemRequest containing name, description, price, category, and availability
     * @return ResponseEntity<MenuItemResponse>
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
     * Delete a menu item from a restaurant
     * @param restaurantId
     * @param id
     * @return ResponseEntity<Void>
     */
    @DeleteMapping("/restaurants/{restaurantId}/menu-items/{id}")
    public ResponseEntity<Void> deleteMenuItem(
            @PathVariable Long restaurantId,
            @PathVariable Long id) {
        menuItemService.deleteMenuItem(restaurantId, id);
        return ResponseEntity.noContent().build();
    }
}