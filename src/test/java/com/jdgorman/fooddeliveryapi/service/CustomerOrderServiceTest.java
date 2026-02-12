package com.jdgorman.fooddeliveryapi.service;

import com.jdgorman.fooddeliveryapi.dto.CustomerOrderRequest;
import com.jdgorman.fooddeliveryapi.dto.CustomerOrderResponse;
import com.jdgorman.fooddeliveryapi.dto.OrderItemRequest;
import com.jdgorman.fooddeliveryapi.dto.OrderStatusUpdateRequest;
import com.jdgorman.fooddeliveryapi.entity.*;
import com.jdgorman.fooddeliveryapi.enumerator.MenuCategory;
import com.jdgorman.fooddeliveryapi.enumerator.CustomerOrderStatus;
import com.jdgorman.fooddeliveryapi.exception.ResourceNotFoundException;
import com.jdgorman.fooddeliveryapi.repository.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CustomerOrderServiceTest {

    @Mock
    private CustomerOrderRepository customerOrderRepository;

    @Mock
    private CustomerRepository customerRepository;

    @Mock
    private RestaurantRepository restaurantRepository;

    @Mock
    private DeliveryAddressRepository deliveryAddressRepository;

    @Mock
    private MenuItemRepository menuItemRepository;

    @InjectMocks
    private CustomerOrderService customerOrderService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @DisplayName("should create customer order successfully")
    void shouldCreateCustomerOrderSuccessfully() {
        CustomerOrderRequest request = new CustomerOrderRequest();
        request.setCustomerId(1L);
        request.setRestaurantId(1L);
        request.setDeliveryAddressId(1L);
        request.setItems(List.of(new OrderItemRequest(2L, 2)));

        Customer customer = Customer.builder()
                .id(1L)
                .firstName("Jane")
                .lastName("Doe")
                .email("jane.doe@example.com")
                .phone("555-123-4567")
                .build();

        Restaurant restaurant = Restaurant.builder()
                .id(1L)
                .name("Test Restaurant")
                .address("123 Test St")
                .phone("555-123-4567")
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

        MenuItem menuItem = MenuItem.builder()
                .id(2L)
                .name("Burger")
                .price(BigDecimal.valueOf(10.00))
                .category(MenuCategory.ENTREE)
                .isAvailable(true)
                .restaurant(restaurant)
                .build();

        CustomerOrder savedOrder = CustomerOrder.builder()
                .id(1L)
                .customer(customer)
                .restaurant(restaurant)
                .deliveryAddress(address)
                .orderItems(List.of(OrderItem.builder()
                        .id(1L)
                        .order(new CustomerOrder())
                        .menuItem(menuItem)
                        .quantity(2)
                        .priceAtOrder(BigDecimal.valueOf(10.00))
                        .subtotal(BigDecimal.valueOf(20.00))
                        .build()))
                .subtotal(BigDecimal.valueOf(20.00))
                .tax(BigDecimal.valueOf(1.65))
                .deliveryFee(BigDecimal.valueOf(5.00))
                .total(BigDecimal.valueOf(26.65))
                .status(CustomerOrderStatus.PENDING)
                .build();

        when(customerRepository.findById(1L)).thenReturn(Optional.of(customer));
        when(restaurantRepository.findById(1L)).thenReturn(Optional.of(restaurant));
        when(deliveryAddressRepository.findById(1L)).thenReturn(Optional.of(address));
        when(menuItemRepository.findAllById(List.of(2L))).thenReturn(List.of(menuItem));
        when(customerOrderRepository.save(any(CustomerOrder.class))).thenReturn(savedOrder);

        CustomerOrderResponse response = customerOrderService.createCustomerOrder(request);

        assertNotNull(response);
        assertEquals(1L, response.getId());
        assertEquals(CustomerOrderStatus.PENDING, response.getStatus());
        assertEquals(BigDecimal.valueOf(26.65), response.getTotal());
    }

    @Test
    @DisplayName("should throw exception when customer order not found")
    void shouldThrowExceptionWhenCustomerOrderNotFound() {
        when(customerOrderRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> customerOrderService.getCustomerOrderById(1L));
    }

    @Test
    @DisplayName("should update customer order status successfully")
    void shouldUpdateCustomerOrderStatusSuccessfully() {
        Customer customer = Customer.builder()
                .id(1L)
                .firstName("Jane")
                .lastName("Doe")
                .email("jane.doe@example.com")
                .phone("555-123-4567")
                .build();

        Restaurant restaurant = Restaurant.builder()
                .id(1L)
                .name("Test Restaurant")
                .address("123 Test St")
                .phone("555-123-4567")
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

        MenuItem menuItem = MenuItem.builder()
                .id(2L)
                .name("Burger")
                .price(BigDecimal.valueOf(10.00))
                .category(MenuCategory.ENTREE)
                .isAvailable(true)
                .restaurant(restaurant)
                .build();

        CustomerOrder existingOrder = CustomerOrder.builder()
                .id(1L)
                .customer(customer)
                .restaurant(restaurant)
                .deliveryAddress(address)
                .orderItems(List.of(OrderItem.builder()
                        .id(1L)
                        .order(new CustomerOrder())
                        .menuItem(menuItem)
                        .quantity(2)
                        .priceAtOrder(BigDecimal.valueOf(10.00))
                        .subtotal(BigDecimal.valueOf(20.00))
                        .build()))
                .subtotal(BigDecimal.valueOf(20.00))
                .tax(BigDecimal.valueOf(1.65))
                .deliveryFee(BigDecimal.valueOf(5.00))
                .total(BigDecimal.valueOf(26.65))
                .status(CustomerOrderStatus.PENDING)
                .build();

        CustomerOrder savedOrder = CustomerOrder.builder()
                .id(1L)
                .customer(customer)
                .restaurant(restaurant)
                .deliveryAddress(address)
                .orderItems(existingOrder.getOrderItems())
                .subtotal(existingOrder.getSubtotal())
                .tax(existingOrder.getTax())
                .deliveryFee(existingOrder.getDeliveryFee())
                .total(existingOrder.getTotal())
                .status(CustomerOrderStatus.CONFIRMED)
                .build();

        when(customerOrderRepository.findById(1L)).thenReturn(Optional.of(existingOrder));
        when(customerOrderRepository.save(any(CustomerOrder.class))).thenReturn(savedOrder);

        OrderStatusUpdateRequest request = new OrderStatusUpdateRequest();
        request.setStatus(CustomerOrderStatus.CONFIRMED);

        CustomerOrderResponse response = customerOrderService.updateCustomerOrderStatus(1L, request);

        assertNotNull(response);
        assertEquals(CustomerOrderStatus.CONFIRMED, response.getStatus());
    }

    @Test
    @DisplayName("should throw exception when updating from terminal state DELIVERED")
    void shouldThrowExceptionWhenUpdatingFromTerminalStateDelivered() {
        Customer customer = Customer.builder()
                .id(1L)
                .firstName("Jane")
                .lastName("Doe")
                .build();

        Restaurant restaurant = Restaurant.builder()
                .id(1L)
                .name("Test Restaurant")
                .build();

        DeliveryAddress address = DeliveryAddress.builder()
                .id(1L)
                .customer(customer)
                .streetAddress("123 Main St")
                .city("Dallas")
                .state("TX")
                .zipCode("75001")
                .build();

        CustomerOrder deliveredOrder = CustomerOrder.builder()
                .id(1L)
                .customer(customer)
                .restaurant(restaurant)
                .deliveryAddress(address)
                .orderItems(List.of())
                .subtotal(BigDecimal.valueOf(20.00))
                .tax(BigDecimal.valueOf(1.65))
                .deliveryFee(BigDecimal.valueOf(5.00))
                .total(BigDecimal.valueOf(26.65))
                .status(CustomerOrderStatus.DELIVERED)
                .build();

        when(customerOrderRepository.findById(1L)).thenReturn(Optional.of(deliveredOrder));

        OrderStatusUpdateRequest request = new OrderStatusUpdateRequest();
        request.setStatus(CustomerOrderStatus.CANCELLED);

        IllegalStateException exception = assertThrows(IllegalStateException.class, () ->
                customerOrderService.updateCustomerOrderStatus(1L, request));

        assertEquals("Cannot update order status from DELIVERED", exception.getMessage());
        verify(customerOrderRepository, never()).save(any(CustomerOrder.class));
    }

    @Test
    @DisplayName("should throw exception when updating from terminal state CANCELLED")
    void shouldThrowExceptionWhenUpdatingFromTerminalStateCancelled() {
        Customer customer = Customer.builder()
                .id(1L)
                .firstName("Jane")
                .lastName("Doe")
                .build();

        Restaurant restaurant = Restaurant.builder()
                .id(1L)
                .name("Test Restaurant")
                .build();

        DeliveryAddress address = DeliveryAddress.builder()
                .id(1L)
                .customer(customer)
                .streetAddress("123 Main St")
                .city("Dallas")
                .state("TX")
                .zipCode("75001")
                .build();

        CustomerOrder cancelledOrder = CustomerOrder.builder()
                .id(1L)
                .customer(customer)
                .restaurant(restaurant)
                .deliveryAddress(address)
                .orderItems(List.of())
                .subtotal(BigDecimal.valueOf(20.00))
                .tax(BigDecimal.valueOf(1.65))
                .deliveryFee(BigDecimal.valueOf(5.00))
                .total(BigDecimal.valueOf(26.65))
                .status(CustomerOrderStatus.CANCELLED)
                .build();

        when(customerOrderRepository.findById(1L)).thenReturn(Optional.of(cancelledOrder));

        OrderStatusUpdateRequest request = new OrderStatusUpdateRequest();
        request.setStatus(CustomerOrderStatus.CONFIRMED);

        IllegalStateException exception = assertThrows(IllegalStateException.class, () ->
                customerOrderService.updateCustomerOrderStatus(1L, request));

        assertEquals("Cannot update order status from CANCELLED", exception.getMessage());
        verify(customerOrderRepository, never()).save(any(CustomerOrder.class));
    }

    @Test
    @DisplayName("should throw exception when attempting backward transition")
    void shouldThrowExceptionWhenAttemptingBackwardTransition() {
        Customer customer = Customer.builder()
                .id(1L)
                .firstName("Jane")
                .lastName("Doe")
                .build();

        Restaurant restaurant = Restaurant.builder()
                .id(1L)
                .name("Test Restaurant")
                .build();

        DeliveryAddress address = DeliveryAddress.builder()
                .id(1L)
                .customer(customer)
                .streetAddress("123 Main St")
                .city("Dallas")
                .state("TX")
                .zipCode("75001")
                .build();

        CustomerOrder readyOrder = CustomerOrder.builder()
                .id(1L)
                .customer(customer)
                .restaurant(restaurant)
                .deliveryAddress(address)
                .orderItems(List.of())
                .subtotal(BigDecimal.valueOf(20.00))
                .tax(BigDecimal.valueOf(1.65))
                .deliveryFee(BigDecimal.valueOf(5.00))
                .total(BigDecimal.valueOf(26.65))
                .status(CustomerOrderStatus.READY)
                .build();

        when(customerOrderRepository.findById(1L)).thenReturn(Optional.of(readyOrder));

        OrderStatusUpdateRequest request = new OrderStatusUpdateRequest();
        request.setStatus(CustomerOrderStatus.CONFIRMED);

        IllegalStateException exception = assertThrows(IllegalStateException.class, () ->
                customerOrderService.updateCustomerOrderStatus(1L, request));

        assertEquals("Cannot change status from READY to CONFIRMED", exception.getMessage());
        verify(customerOrderRepository, never()).save(any(CustomerOrder.class));
    }

    @Test
    @DisplayName("should throw exception when attempting to cancel from PREPARING state")
    void shouldThrowExceptionWhenAttemptingToCancelFromPreparingState() {
        Customer customer = Customer.builder()
                .id(1L)
                .firstName("Jane")
                .lastName("Doe")
                .build();

        Restaurant restaurant = Restaurant.builder()
                .id(1L)
                .name("Test Restaurant")
                .build();

        DeliveryAddress address = DeliveryAddress.builder()
                .id(1L)
                .customer(customer)
                .streetAddress("123 Main St")
                .city("Dallas")
                .state("TX")
                .zipCode("75001")
                .build();

        CustomerOrder preparingOrder = CustomerOrder.builder()
                .id(1L)
                .customer(customer)
                .restaurant(restaurant)
                .deliveryAddress(address)
                .orderItems(List.of())
                .subtotal(BigDecimal.valueOf(20.00))
                .tax(BigDecimal.valueOf(1.65))
                .deliveryFee(BigDecimal.valueOf(5.00))
                .total(BigDecimal.valueOf(26.65))
                .status(CustomerOrderStatus.PREPARING)
                .build();

        when(customerOrderRepository.findById(1L)).thenReturn(Optional.of(preparingOrder));

        OrderStatusUpdateRequest request = new OrderStatusUpdateRequest();
        request.setStatus(CustomerOrderStatus.CANCELLED);

        IllegalStateException exception = assertThrows(IllegalStateException.class, () ->
                customerOrderService.updateCustomerOrderStatus(1L, request));

        assertEquals("Cannot cancel order. Order is already PREPARING", exception.getMessage());
        verify(customerOrderRepository, never()).save(any(CustomerOrder.class));
    }

    @Test
    @DisplayName("should allow transition to CANCELLED from PENDING state")
    void shouldAllowTransitionToCancelledFromPendingState() {
        Customer customer = Customer.builder()
                .id(1L)
                .firstName("Jane")
                .lastName("Doe")
                .build();

        Restaurant restaurant = Restaurant.builder()
                .id(1L)
                .name("Test Restaurant")
                .build();

        DeliveryAddress address = DeliveryAddress.builder()
                .id(1L)
                .customer(customer)
                .streetAddress("123 Main St")
                .city("Dallas")
                .state("TX")
                .zipCode("75001")
                .build();

        MenuItem menuItem = MenuItem.builder()
                .id(2L)
                .name("Burger")
                .price(BigDecimal.valueOf(10.00))
                .category(MenuCategory.ENTREE)
                .restaurant(restaurant)
                .build();

        CustomerOrder pendingOrder = CustomerOrder.builder()
                .id(1L)
                .customer(customer)
                .restaurant(restaurant)
                .deliveryAddress(address)
                .orderItems(List.of(OrderItem.builder()
                        .id(1L)
                        .order(new CustomerOrder())
                        .menuItem(menuItem)
                        .quantity(2)
                        .priceAtOrder(BigDecimal.valueOf(10.00))
                        .subtotal(BigDecimal.valueOf(20.00))
                        .build()))
                .subtotal(BigDecimal.valueOf(20.00))
                .tax(BigDecimal.valueOf(1.65))
                .deliveryFee(BigDecimal.valueOf(5.00))
                .total(BigDecimal.valueOf(26.65))
                .status(CustomerOrderStatus.PENDING)
                .build();

        CustomerOrder cancelledOrder = CustomerOrder.builder()
                .id(1L)
                .customer(customer)
                .restaurant(restaurant)
                .deliveryAddress(address)
                .orderItems(pendingOrder.getOrderItems())
                .subtotal(pendingOrder.getSubtotal())
                .tax(pendingOrder.getTax())
                .deliveryFee(pendingOrder.getDeliveryFee())
                .total(pendingOrder.getTotal())
                .status(CustomerOrderStatus.CANCELLED)
                .build();

        when(customerOrderRepository.findById(1L)).thenReturn(Optional.of(pendingOrder));
        when(customerOrderRepository.save(any(CustomerOrder.class))).thenReturn(cancelledOrder);

        OrderStatusUpdateRequest request = new OrderStatusUpdateRequest();
        request.setStatus(CustomerOrderStatus.CANCELLED);

        CustomerOrderResponse response = customerOrderService.updateCustomerOrderStatus(1L, request);

        assertNotNull(response);
        assertEquals(CustomerOrderStatus.CANCELLED, response.getStatus());
        verify(customerOrderRepository).save(any(CustomerOrder.class));
    }

    @Test
    @DisplayName("should throw exception when cancelling already delivered order")
    void shouldThrowExceptionWhenCancellingAlreadyDeliveredOrder() {
        CustomerOrder deliveredOrder = CustomerOrder.builder()
                .id(1L)
                .status(CustomerOrderStatus.DELIVERED)
                .build();

        when(customerOrderRepository.findById(1L)).thenReturn(Optional.of(deliveredOrder));

        IllegalStateException exception = assertThrows(IllegalStateException.class, () ->
                customerOrderService.cancelCustomerOrder(1L));

        assertEquals("Cannot cancel order. Order is already DELIVERED", exception.getMessage());
        verify(customerOrderRepository, never()).save(any(CustomerOrder.class));
    }

    @Test
    @DisplayName("should throw exception when cancelling order in OUT_FOR_DELIVERY status")
    void shouldThrowExceptionWhenCancellingOrderInOutForDeliveryStatus() {
        CustomerOrder outForDeliveryOrder = CustomerOrder.builder()
                .id(1L)
                .status(CustomerOrderStatus.OUT_FOR_DELIVERY)
                .build();

        when(customerOrderRepository.findById(1L)).thenReturn(Optional.of(outForDeliveryOrder));

        IllegalStateException exception = assertThrows(IllegalStateException.class, () ->
                customerOrderService.cancelCustomerOrder(1L));

        assertEquals("Cannot cancel order. Order is already OUT_FOR_DELIVERY", exception.getMessage());
        verify(customerOrderRepository, never()).save(any(CustomerOrder.class));
    }
}
