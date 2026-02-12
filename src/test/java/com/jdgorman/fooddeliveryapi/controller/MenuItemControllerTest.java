package com.jdgorman.fooddeliveryapi.controller;

import com.jdgorman.fooddeliveryapi.dto.MenuItemRequest;
import com.jdgorman.fooddeliveryapi.dto.MenuItemResponse;
import com.jdgorman.fooddeliveryapi.enumerator.MenuCategory;
import com.jdgorman.fooddeliveryapi.service.MenuItemService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class MenuItemControllerTest {

    @Mock
    private MenuItemService menuItemService;

    @InjectMocks
    private MenuItemController menuItemController;

    @Test
    void getMenuItemsByRestaurantReturnsListOfMenuItems() {
        Long restaurantId = 1L;
        List<MenuItemResponse> menuItems = List.of(MenuItemResponse.builder().build());
        when(menuItemService.getMenuItemsByRestaurant(restaurantId)).thenReturn(menuItems);

        var response = menuItemController.getMenuItemsByRestaurant(restaurantId, null);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(menuItems, response.getBody());
    }

    @Test
    void getMenuItemsByRestaurantAndCategoryReturnsFilteredMenuItems() {
        Long restaurantId = 1L;
        MenuCategory category = MenuCategory.APPETIZER;
        List<MenuItemResponse> menuItems = List.of(MenuItemResponse.builder().build());
        when(menuItemService.getMenuItemsByRestaurantAndCategory(restaurantId, category)).thenReturn(menuItems);

        var response = menuItemController.getMenuItemsByRestaurant(restaurantId, category);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(menuItems, response.getBody());
    }

    @Test
    void getMenuItemByIdReturnsMenuItem() {
        Long restaurantId = 1L;
        Long id = 2L;
        MenuItemResponse menuItem = MenuItemResponse.builder().build();
        when(menuItemService.getMenuItemById(restaurantId, id)).thenReturn(menuItem);

        var response = menuItemController.getMenuItemById(restaurantId, id);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(menuItem, response.getBody());
    }

    @Test
    void createMenuItemReturnsCreatedMenuItem() {
        Long restaurantId = 1L;
        MenuItemRequest request = MenuItemRequest.builder().build();
        MenuItemResponse createdMenuItem = MenuItemResponse.builder().build();
        when(menuItemService.createMenuItem(restaurantId, request)).thenReturn(createdMenuItem);

        var response = menuItemController.createMenuItem(restaurantId, request);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(createdMenuItem, response.getBody());
    }

    @Test
    void updateMenuItemReturnsUpdatedMenuItem() {
        Long restaurantId = 1L;
        Long id = 2L;
        MenuItemRequest request = MenuItemRequest.builder().build();
        MenuItemResponse updatedMenuItem = MenuItemResponse.builder().build();
        when(menuItemService.updateMenuItem(restaurantId, id, request)).thenReturn(updatedMenuItem);

        var response = menuItemController.updateMenuItem(restaurantId, id, request);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(updatedMenuItem, response.getBody());
    }


    @Test
    void deleteMenuItemReturnsNoContent() {
        Long restaurantId = 1L;
        Long id = 2L;

        var response = menuItemController.deleteMenuItem(restaurantId, id);

        verify(menuItemService).deleteMenuItem(restaurantId, id);
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
    }
}
