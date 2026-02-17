package com.jdgorman.fooddeliveryapi.repository;

import com.jdgorman.fooddeliveryapi.entity.Customer;
import com.jdgorman.fooddeliveryapi.entity.CustomerOrder;
import com.jdgorman.fooddeliveryapi.entity.Delivery;
import com.jdgorman.fooddeliveryapi.entity.DeliveryAddress;
import com.jdgorman.fooddeliveryapi.entity.Driver;
import com.jdgorman.fooddeliveryapi.entity.Restaurant;
import com.jdgorman.fooddeliveryapi.enumerator.CustomerOrderStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class DeliveryRepositoryTest {

    @Autowired
    private DeliveryRepository deliveryRepository;

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private RestaurantRepository restaurantRepository;

    @Autowired
    private DeliveryAddressRepository deliveryAddressRepository;

    @Autowired
    private CustomerOrderRepository customerOrderRepository;

    @Autowired
    private DriverRepository driverRepository;

    private Delivery delivery;
    private CustomerOrder customerOrder;
    private Driver driver;

    @BeforeEach
    void setUp() {
        Customer customer = customerRepository.save(Customer.builder()
                .firstName("John")
                .lastName("Doe")
                .email("john.doe@example.com")
                .phone("555-123-4567")
                .build());

        Restaurant restaurant = restaurantRepository.save(Restaurant.builder()
                .name("Test Restaurant")
                .address("123 Test St")
                .phone("555-987-6543")
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

        customerOrder = customerOrderRepository.save(CustomerOrder.builder()
                .customer(customer)
                .restaurant(restaurant)
                .deliveryAddress(address)
                .subtotal(BigDecimal.valueOf(50.00))
                .tax(BigDecimal.valueOf(5.00))
                .deliveryFee(BigDecimal.valueOf(3.00))
                .total(BigDecimal.valueOf(58.00))
                .status(CustomerOrderStatus.PENDING)
                .build());

        driver = driverRepository.save(Driver.builder()
                .firstName("Alex")
                .lastName("Taylor")
                .email("alex.taylor@example.com")
                .phone("555-555-0101")
                .vehicleType("Car")
                .build());

        delivery = deliveryRepository.save(Delivery.builder()
                .customerOrder(customerOrder)
                .driver(driver)
                .status(CustomerOrderStatus.PENDING)
                .build());
    }

    @Test
    void shouldFindDeliveryByCustomerOrderId() {
        Optional<Delivery> result = deliveryRepository.findByCustomerOrderId(customerOrder.getId());
        assertTrue(result.isPresent());
        assertEquals(delivery.getId(), result.get().getId());
    }

    @Test
    void shouldReturnEmptyWhenCustomerOrderIdDoesNotExist() {
        Optional<Delivery> result = deliveryRepository.findByCustomerOrderId(999L);
        assertTrue(result.isEmpty());
    }

    @Test
    void shouldFindDeliveriesByDriverId() {
        List<Delivery> result = deliveryRepository.findByDriverId(driver.getId());
        assertEquals(1, result.size());
        assertEquals(delivery.getId(), result.get(0).getId());
    }

    @Test
    void shouldReturnEmptyListWhenDriverIdDoesNotExist() {
        List<Delivery> result = deliveryRepository.findByDriverId(999L);
        assertTrue(result.isEmpty());
    }

    @Test
    void shouldFindDeliveriesByDriverIdAndStatus() {
        List<Delivery> result = deliveryRepository.findByDriverIdAndStatus(driver.getId(), CustomerOrderStatus.PENDING);
        assertEquals(1, result.size());
        assertEquals(delivery.getId(), result.get(0).getId());
    }

    @Test
    void shouldReturnEmptyListWhenDriverIdAndStatusDoNotMatch() {
        List<Delivery> result = deliveryRepository.findByDriverIdAndStatus(driver.getId(), CustomerOrderStatus.DELIVERED);
        assertTrue(result.isEmpty());
    }

    @Test
    void shouldCheckExistenceByCustomerOrderId() {
        boolean exists = deliveryRepository.existsByCustomerOrderId(customerOrder.getId());
        assertTrue(exists);
    }

    @Test
    void shouldReturnFalseWhenCustomerOrderIdDoesNotExist() {
        boolean exists = deliveryRepository.existsByCustomerOrderId(999L);
        assertFalse(exists);
    }
}
