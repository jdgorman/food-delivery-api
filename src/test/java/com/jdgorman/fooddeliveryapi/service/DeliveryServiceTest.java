package com.jdgorman.fooddeliveryapi.service;

import com.jdgorman.fooddeliveryapi.dto.DeliveryRequest;
import com.jdgorman.fooddeliveryapi.dto.DeliveryResponse;
import com.jdgorman.fooddeliveryapi.entity.Customer;
import com.jdgorman.fooddeliveryapi.entity.CustomerOrder;
import com.jdgorman.fooddeliveryapi.entity.Delivery;
import com.jdgorman.fooddeliveryapi.entity.DeliveryAddress;
import com.jdgorman.fooddeliveryapi.entity.Driver;
import com.jdgorman.fooddeliveryapi.entity.Restaurant;
import com.jdgorman.fooddeliveryapi.enumerator.CustomerOrderStatus;
import com.jdgorman.fooddeliveryapi.enumerator.DriverStatus;
import com.jdgorman.fooddeliveryapi.exception.ResourceNotFoundException;
import com.jdgorman.fooddeliveryapi.repository.CustomerOrderRepository;
import com.jdgorman.fooddeliveryapi.repository.DeliveryRepository;
import com.jdgorman.fooddeliveryapi.repository.DriverRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class DeliveryServiceTest {

    @Mock
    private DeliveryRepository deliveryRepository;

    @Mock
    private CustomerOrderRepository customerOrderRepository;

    @Mock
    private DriverRepository driverRepository;

    @InjectMocks
    private DeliveryService deliveryService;

    private CustomerOrder order;
    private Driver driver;
    private Delivery delivery;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        Customer customer = Customer.builder()
                .id(1L)
                .firstName("Alex")
                .lastName("Taylor")
                .email("alex.taylor@example.com")
                .phone("555-123-4567")
                .build();

        Restaurant restaurant = Restaurant.builder()
                .id(1L)
                .name("Test Restaurant")
                .address("123 Test St")
                .phone("555-987-6543")
                .cuisineType("Test Cuisine")
                .build();

        DeliveryAddress address = DeliveryAddress.builder()
                .id(1L)
                .customer(customer)
                .label("Home")
                .streetAddress("123 Main St")
                .city("Dallas")
                .state("TX")
                .zipCode("75001")
                .isDefault(true)
                .build();

        order = CustomerOrder.builder()
                .id(1L)
                .customer(customer)
                .restaurant(restaurant)
                .deliveryAddress(address)
                .status(CustomerOrderStatus.READY)
                .build();

        driver = Driver.builder()
                .id(1L)
                .firstName("Jamie")
                .lastName("Wells")
                .email("jamie.wells@example.com")
                .phone("555-555-0101")
                .vehicleType("Car")
                .status(DriverStatus.AVAILABLE)
                .build();

        delivery = Delivery.builder()
                .id(1L)
                .customerOrder(order)
                .driver(driver)
                .status(CustomerOrderStatus.READY)
                .createTimestamp(LocalDateTime.now())
                .updateTimestamp(LocalDateTime.now())
                .build();
    }

    @Test
    void shouldCreateDeliverySuccessfully() {
        DeliveryRequest request = DeliveryRequest.builder()
                .orderId(1L)
                .driverId(1L)
                .estimatedDeliveryTime(LocalDateTime.now().plusMinutes(30))
                .build();

        when(customerOrderRepository.findById(1L)).thenReturn(Optional.of(order));
        when(deliveryRepository.existsByCustomerOrderId(1L)).thenReturn(false);
        when(driverRepository.findById(1L)).thenReturn(Optional.of(driver));
        when(driverRepository.save(any(Driver.class))).thenReturn(driver);
        when(deliveryRepository.save(any(Delivery.class))).thenAnswer(invocation -> {
            Delivery saved = invocation.getArgument(0);
            saved.setId(1L);
            return saved;
        });

        DeliveryResponse result = deliveryService.createDelivery(request);

        assertNotNull(result);
        assertEquals(1L, result.getOrderId());
        assertEquals(1L, result.getDriverId());
        assertEquals(CustomerOrderStatus.READY, result.getStatus());
        assertEquals(DriverStatus.BUSY, driver.getStatus());
    }

    @Test
    void shouldThrowWhenCreatingDeliveryForMissingOrder() {
        DeliveryRequest request = DeliveryRequest.builder()
                .orderId(1L)
                .driverId(1L)
                .build();

        when(customerOrderRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> deliveryService.createDelivery(request));
    }

    @Test
    void shouldThrowWhenCreatingDeliveryForUnavailableDriver() {
        DeliveryRequest request = DeliveryRequest.builder()
                .orderId(1L)
                .driverId(1L)
                .build();

        driver.setStatus(DriverStatus.BUSY);
        when(customerOrderRepository.findById(1L)).thenReturn(Optional.of(order));
        when(deliveryRepository.existsByCustomerOrderId(1L)).thenReturn(false);
        when(driverRepository.findById(1L)).thenReturn(Optional.of(driver));

        assertThrows(IllegalStateException.class, () -> deliveryService.createDelivery(request));
    }

    @Test
    void shouldGetDeliveryByIdSuccessfully() {
        when(deliveryRepository.findByIdWithAllAssociations(1L)).thenReturn(Optional.of(delivery));

        DeliveryResponse result = deliveryService.getDeliveryById(1L);

        assertNotNull(result);
        assertEquals(1L, result.getOrderId());
        assertEquals(1L, result.getDriverId());
    }

    @Test
    void shouldGetDeliveryByOrderIdSuccessfully() {
        when(deliveryRepository.findByCustomerOrderIdWithAllAssociations(1L)).thenReturn(Optional.of(delivery));

        DeliveryResponse result = deliveryService.getDeliveryByOrderId(1L);

        assertNotNull(result);
        assertEquals(1L, result.getOrderId());
        assertEquals(1L, result.getDriverId());
    }

    @Test
    void shouldGetDeliveriesByDriverSuccessfully() {
        when(driverRepository.findById(1L)).thenReturn(Optional.of(driver));
        when(deliveryRepository.findByDriverIdWithAllAssociationsOrderByCreateTimestampDesc(1L)).thenReturn(List.of(delivery));

        List<DeliveryResponse> result = deliveryService.getDeliveriesByDriver(1L);

        assertEquals(1, result.size());
        assertEquals(1L, result.get(0).getDriverId());
    }

    @Test
    void shouldMarkDeliveryAsPickedUpSuccessfully() {
        when(deliveryRepository.findById(1L)).thenReturn(Optional.of(delivery));
        when(customerOrderRepository.save(any(CustomerOrder.class))).thenReturn(order);
        when(deliveryRepository.save(any(Delivery.class))).thenAnswer(invocation -> invocation.getArgument(0));

        DeliveryResponse result = deliveryService.markAsPickedUp(1L);

        assertEquals(CustomerOrderStatus.OUT_FOR_DELIVERY, result.getStatus());
        assertEquals(CustomerOrderStatus.OUT_FOR_DELIVERY, order.getStatus());
        assertNotNull(result.getPickupTimestamp());
    }

    @Test
    void shouldMarkDeliveryAsDeliveredSuccessfully() {
        delivery.setStatus(CustomerOrderStatus.OUT_FOR_DELIVERY);
        when(deliveryRepository.findById(1L)).thenReturn(Optional.of(delivery));
        when(customerOrderRepository.save(any(CustomerOrder.class))).thenReturn(order);
        when(driverRepository.save(any(Driver.class))).thenReturn(driver);
        when(deliveryRepository.save(any(Delivery.class))).thenAnswer(invocation -> invocation.getArgument(0));

        DeliveryResponse result = deliveryService.markAsDelivered(1L);

        assertEquals(CustomerOrderStatus.DELIVERED, result.getStatus());
        assertEquals(CustomerOrderStatus.DELIVERED, order.getStatus());
        assertEquals(DriverStatus.AVAILABLE, driver.getStatus());
        assertNotNull(result.getDeliveryTimestamp());
    }
}
