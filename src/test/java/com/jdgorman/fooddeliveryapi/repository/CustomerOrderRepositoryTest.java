package com.jdgorman.fooddeliveryapi.repository;

import com.jdgorman.fooddeliveryapi.entity.Customer;
import com.jdgorman.fooddeliveryapi.entity.CustomerOrder;
import com.jdgorman.fooddeliveryapi.entity.DeliveryAddress;
import com.jdgorman.fooddeliveryapi.entity.Restaurant;
import com.jdgorman.fooddeliveryapi.enumerator.CustomerOrderStatus;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class CustomerOrderRepositoryTest {

    @Autowired
    private CustomerOrderRepository customerOrderRepository;

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private RestaurantRepository restaurantRepository;

    @Autowired
    private DeliveryAddressRepository deliveryAddressRepository;

    private CustomerOrder createOrder(CustomerOrderStatus status) {
        Customer customer = customerRepository.save(Customer.builder()
                .firstName("John")
                .lastName("Doe")
                .email("john.doe@example.com")
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
                .subtotal(BigDecimal.valueOf(50.00))
                .tax(BigDecimal.valueOf(5.00))
                .deliveryFee(BigDecimal.valueOf(3.00))
                .total(BigDecimal.valueOf(58.00))
                .status(status)
                .build());
    }

    @Test
    @DisplayName("should find orders by customer ID")
    void shouldFindOrdersByCustomerId() {
        CustomerOrder order = createOrder(CustomerOrderStatus.PENDING);
        List<CustomerOrder> orders = customerOrderRepository.findByCustomerId(order.getCustomer().getId());

        assertFalse(orders.isEmpty());
        assertEquals(order.getCustomer().getId(), orders.get(0).getCustomer().getId());
    }

    @Test
    @DisplayName("should find orders by restaurant ID")
    void shouldFindOrdersByRestaurantId() {
        CustomerOrder order = createOrder(CustomerOrderStatus.PENDING);
        List<CustomerOrder> orders = customerOrderRepository.findByRestaurantId(order.getRestaurant().getId());

        assertFalse(orders.isEmpty());
        assertEquals(order.getRestaurant().getId(), orders.get(0).getRestaurant().getId());
    }

    @Test
    @DisplayName("should find orders by status")
    void shouldFindOrdersByStatus() {
        CustomerOrder order = createOrder(CustomerOrderStatus.CONFIRMED);
        List<CustomerOrder> orders = customerOrderRepository.findByStatus(CustomerOrderStatus.CONFIRMED);

        assertFalse(orders.isEmpty());
        assertEquals(CustomerOrderStatus.CONFIRMED, orders.get(0).getStatus());
    }

    @Test
    @DisplayName("should find orders by restaurant ID and status")
    void shouldFindOrdersByRestaurantIdAndStatus() {
        CustomerOrder order = createOrder(CustomerOrderStatus.READY);
        List<CustomerOrder> orders = customerOrderRepository.findByRestaurantIdAndStatus(order.getRestaurant().getId(), CustomerOrderStatus.READY);

        assertFalse(orders.isEmpty());
        assertEquals(order.getRestaurant().getId(), orders.get(0).getRestaurant().getId());
        assertEquals(CustomerOrderStatus.READY, orders.get(0).getStatus());
    }
}
