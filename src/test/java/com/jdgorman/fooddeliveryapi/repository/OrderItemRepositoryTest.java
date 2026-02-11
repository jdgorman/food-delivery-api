package com.jdgorman.fooddeliveryapi.repository;

import com.jdgorman.fooddeliveryapi.entity.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;


import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class OrderItemRepositoryTest {

    @Autowired
    private OrderItemRepository orderItemRepository;

    @Autowired
    private CustomerOrderRepository customerOrderRepository;

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private RestaurantRepository restaurantRepository;

    @Autowired
    private DeliveryAddressRepository deliveryAddressRepository;

    @Autowired
    private MenuItemRepository menuItemRepository;

    private CustomerOrder buildOrderWithRelations() {
        Customer customer = customerRepository.save(Customer.builder()
                .firstName("Jane")
                .lastName("Doe")
                .email("jane.doe@example.com")
                .phone("555-123-4567")
                .build());

        Restaurant restaurant = restaurantRepository.save(Restaurant.builder()
                .name("Test Restaurant")
                .address("123 Test St")
                .phone("555-123-4567")
                .cuisineType("Test Cuisine")
                .build());

        DeliveryAddress address = deliveryAddressRepository.save(DeliveryAddress.builder()
                .customer(customer)
                .label("Home")
                .streetAddress("123 Main St")
                .city("Dallas")
                .state("TX")
                .zipCode("75001")
                .isDefault(true)
                .build());

        return customerOrderRepository.save(CustomerOrder.builder()
                .customer(customer)
                .restaurant(restaurant)
                .deliveryAddress(address)
                .subtotal(BigDecimal.ZERO)
                .tax(BigDecimal.ZERO)
                .deliveryFee(BigDecimal.ZERO)
                .total(BigDecimal.ZERO)
                .build());
    }

    private MenuItem buildMenuItem(Restaurant restaurant) {
        return menuItemRepository.save(MenuItem.builder()
                .name("Burger")
                .description("Test item")
                .price(BigDecimal.valueOf(10.00))
                .category(com.jdgorman.fooddeliveryapi.enumerator.MenuCategory.ENTREE)
                .isAvailable(true)
                .restaurant(restaurant)
                .build());
    }

    @Test
    @DisplayName("should save and retrieve OrderItem successfully")
    void shouldSaveAndRetrieveOrderItemSuccessfully() {
        CustomerOrder order = buildOrderWithRelations();
        MenuItem menuItem = buildMenuItem(order.getRestaurant());

        OrderItem orderItem = OrderItem.builder()
                .order(order)
                .menuItem(menuItem)
                .quantity(2)
                .priceAtOrder(BigDecimal.valueOf(10.00))
                .subtotal(BigDecimal.valueOf(20.00))
                .build();

        OrderItem savedOrderItem = orderItemRepository.save(orderItem);
        Optional<OrderItem> retrievedOrderItem = orderItemRepository.findById(savedOrderItem.getId());

        assertTrue(retrievedOrderItem.isPresent());
        assertEquals(savedOrderItem.getId(), retrievedOrderItem.get().getId());
        assertEquals(2, retrievedOrderItem.get().getQuantity());
        assertEquals(BigDecimal.valueOf(10.00), retrievedOrderItem.get().getPriceAtOrder());
        assertEquals(BigDecimal.valueOf(20.00), retrievedOrderItem.get().getSubtotal());
    }

    @Test
    @DisplayName("should delete OrderItem successfully")
    void shouldDeleteOrderItemSuccessfully() {
        CustomerOrder order = buildOrderWithRelations();
        MenuItem menuItem = buildMenuItem(order.getRestaurant());

        OrderItem orderItem = OrderItem.builder()
                .order(order)
                .menuItem(menuItem)
                .quantity(1)
                .priceAtOrder(BigDecimal.valueOf(5.00))
                .subtotal(BigDecimal.valueOf(5.00))
                .build();

        OrderItem savedOrderItem = orderItemRepository.save(orderItem);
        orderItemRepository.deleteById(savedOrderItem.getId());
        Optional<OrderItem> retrievedOrderItem = orderItemRepository.findById(savedOrderItem.getId());

        assertFalse(retrievedOrderItem.isPresent());
    }
}
