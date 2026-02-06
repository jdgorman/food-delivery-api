package com.jdgorman.fooddeliveryapi.repository;

import com.jdgorman.fooddeliveryapi.entity.MenuCategory;
import com.jdgorman.fooddeliveryapi.entity.MenuItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MenuItemRepository extends JpaRepository<MenuItem, Long> {

    // Find all menu items for a specific restaurant
    List<MenuItem> findByRestaurantId(Long restaurantId);

    // Find items by restaurant and category
    List<MenuItem> findByRestaurantIdAndCategory(Long restaurantId, MenuCategory category);
}