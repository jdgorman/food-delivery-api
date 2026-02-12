package com.jdgorman.fooddeliveryapi.service;

import com.jdgorman.fooddeliveryapi.dto.MenuItemRequest;
import com.jdgorman.fooddeliveryapi.dto.MenuItemResponse;
import com.jdgorman.fooddeliveryapi.enumerator.MenuCategory;
import com.jdgorman.fooddeliveryapi.entity.MenuItem;
import com.jdgorman.fooddeliveryapi.entity.Restaurant;
import com.jdgorman.fooddeliveryapi.exception.ResourceNotFoundException;
import com.jdgorman.fooddeliveryapi.repository.MenuItemRepository;
import com.jdgorman.fooddeliveryapi.repository.RestaurantRepository;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class MenuItemServiceTest {

    @Mock
    private RestaurantRepository restaurantRepository;

    @Mock
    private MenuItemRepository menuItemRepository;

    @InjectMocks
    private MenuItemService menuItemService;

    public MenuItemServiceTest() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getMenuItemsByRestaurantReturnsListWhenRestaurantExists() {
        Restaurant restaurant = Restaurant.builder().id(1L).build();
        when(restaurantRepository.findById(1L)).thenReturn(Optional.of(restaurant));

        MenuItem item1 = MenuItem.builder().restaurant(restaurant).build();
        MenuItem item2 = MenuItem.builder().restaurant(restaurant).build();
        when(menuItemRepository.findByRestaurantId(1L)).thenReturn(List.of(item1, item2));

        List<MenuItemResponse> responses = menuItemService.getMenuItemsByRestaurant(1L);

        assertEquals(2, responses.size());
    }


    @Test
    void getMenuItemsByRestaurantThrowsExceptionWhenRestaurantNotFound() {
        when(restaurantRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> menuItemService.getMenuItemsByRestaurant(1L));
    }

    @Test
    void getMenuItemByIdReturnsMenuItemWhenFoundAndBelongsToRestaurant() {
        Restaurant restaurant = Restaurant.builder().id(1L).build();
        MenuItem menuItem = MenuItem.builder().id(1L).restaurant(restaurant).build();
        when(menuItemRepository.findById(1L)).thenReturn(Optional.of(menuItem));

        MenuItemResponse response = menuItemService.getMenuItemById(1L, 1L);

        assertEquals(1L, response.getId());
    }

    @Test
    void getMenuItemByIdThrowsExceptionWhenNotFound() {
        when(menuItemRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> menuItemService.getMenuItemById(1L, 1L));
    }

    @Test
    void getMenuItemByIdThrowsExceptionWhenMenuItemDoesNotBelongToRestaurant() {
        Restaurant restaurant = Restaurant.builder().id(2L).build();
        MenuItem menuItem = MenuItem.builder().id(1L).restaurant(restaurant).build();
        when(menuItemRepository.findById(1L)).thenReturn(Optional.of(menuItem));

        assertThrows(ResourceNotFoundException.class, () -> menuItemService.getMenuItemById(1L, 1L));
    }

    @Test
    void createMenuItemSavesAndReturnsMenuItemWhenRestaurantExists() {
        Restaurant restaurant = Restaurant.builder().id(1L).build();
        MenuItemRequest request = MenuItemRequest.builder()
                .name("Item")
                .description("Description")
                .price(BigDecimal.TEN)
                .category(MenuCategory.ENTREE)
                .isAvailable(true)
                .build();
        when(restaurantRepository.findById(1L)).thenReturn(Optional.of(restaurant));

        when(menuItemRepository.save(org.mockito.ArgumentMatchers.any(MenuItem.class)))
                .thenAnswer(invocation -> {
                    MenuItem m = invocation.getArgument(0);
                    m.setId(1L);
                    m.setRestaurant(restaurant);
                    return m;
                });

        MenuItemResponse response = menuItemService.createMenuItem(1L, request);

        assertEquals("Item", response.getName());
        assertEquals(1L, response.getRestaurantId());
    }


    @Test
    void createMenuItemThrowsExceptionWhenRestaurantNotFound() {
        MenuItemRequest request = MenuItemRequest.builder()
                .name("Item")
                .description("Description")
                .price(BigDecimal.valueOf(10.0))
                .category(MenuCategory.ENTREE)
                .isAvailable(true)
                .build();
        when(restaurantRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> menuItemService.createMenuItem(1L, request));
    }

    @Test
    void updateMenuItemUpdatesAndReturnsMenuItemWhenFoundAndBelongsToRestaurant() {
        Restaurant restaurant = Restaurant.builder().id(1L).build();
        MenuItem menuItem = MenuItem.builder().id(1L).restaurant(restaurant).build();
        MenuItemRequest request = MenuItemRequest.builder()
                .name("Updated Item")
                .description("Updated Description")
                .price(BigDecimal.valueOf(15.0))
                .category(MenuCategory.ENTREE)
                .isAvailable(false)
                .build();
        when(menuItemRepository.findById(1L)).thenReturn(Optional.of(menuItem));
        when(menuItemRepository.save(menuItem)).thenReturn(menuItem);

        MenuItemResponse response = menuItemService.updateMenuItem(1L, 1L, request);

        assertEquals("Updated Item", response.getName());
        assertEquals(BigDecimal.valueOf(15.0), response.getPrice());
    }

    @Test
    void updateMenuItemThrowsExceptionWhenMenuItemNotFound() {
        MenuItemRequest request = MenuItemRequest.builder()
                .name("Updated Item")
                .description("Updated Description")
                .price(BigDecimal.valueOf(15.0))
                .category(MenuCategory.BEVERAGE)
                .isAvailable(false)
                .build();
        when(menuItemRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> menuItemService.updateMenuItem(1L, 1L, request));
    }

    @Test
    void updateMenuItemThrowsExceptionWhenMenuItemDoesNotBelongToRestaurant() {
        Restaurant restaurant = Restaurant.builder().id(2L).build();
        MenuItem menuItem = MenuItem.builder().id(1L).restaurant(restaurant).build();
        MenuItemRequest request = MenuItemRequest.builder()
                .name("Updated Item")
                .description("Updated Description")
                .price(BigDecimal.valueOf(15.0))
                .category(MenuCategory.BEVERAGE)
                .isAvailable(false)
                .build();
        when(menuItemRepository.findById(1L)).thenReturn(Optional.of(menuItem));

        assertThrows(ResourceNotFoundException.class, () -> menuItemService.updateMenuItem(1L, 1L, request));
    }


    @Test
    void deleteMenuItemDeletesWhenFoundAndBelongsToRestaurant() {
        Restaurant restaurant = Restaurant.builder().id(1L).build();
        MenuItem menuItem = MenuItem.builder().id(1L).restaurant(restaurant).build();
        when(menuItemRepository.findById(1L)).thenReturn(Optional.of(menuItem));

        menuItemService.deleteMenuItem(1L, 1L);

        verify(menuItemRepository).deleteById(1L);
    }

    @Test
    void deleteMenuItemThrowsExceptionWhenMenuItemNotFound() {
        when(menuItemRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> menuItemService.deleteMenuItem(1L, 1L));
    }

    @Test
    void deleteMenuItemThrowsExceptionWhenMenuItemDoesNotBelongToRestaurant() {
        Restaurant restaurant = Restaurant.builder().id(2L).build();
        MenuItem menuItem = MenuItem.builder().id(1L).restaurant(restaurant).build();
        when(menuItemRepository.findById(1L)).thenReturn(Optional.of(menuItem));

        assertThrows(ResourceNotFoundException.class, () -> menuItemService.deleteMenuItem(1L, 1L));
    }

}
