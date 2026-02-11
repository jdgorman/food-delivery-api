package com.jdgorman.fooddeliveryapi.controller;

import com.jdgorman.fooddeliveryapi.dto.*;
import com.jdgorman.fooddeliveryapi.service.CustomerOrderService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class OrderControllerTest {

    @Mock
    private CustomerOrderService customerOrderService;

    @InjectMocks
    private OrderController orderController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Nested
    @DisplayName("createOrder")
    class CreateOrder {

        @Test
        @DisplayName("should return 201 and created order when request is valid")
        void shouldReturn201AndCreatedOrderWhenRequestIsValid() {
            CustomerOrderRequest request = new CustomerOrderRequest();
            CustomerOrderResponse response = new CustomerOrderResponse();

            when(customerOrderService.createCustomerOrder(request)).thenReturn(response);

            ResponseEntity<CustomerOrderResponse> result = orderController.createOrder(request);

            assertEquals(HttpStatus.CREATED, result.getStatusCode());
            assertEquals(response, result.getBody());
        }
    }

    @Nested
    @DisplayName("getOrderById")
    class GetOrderById {

        @Test
        @DisplayName("should return 200 and order when order exists")
        void shouldReturn200AndOrderWhenOrderExists() {
            Long orderId = 1L;
            CustomerOrderResponse response = new CustomerOrderResponse();

            when(customerOrderService.getCustomerOrderById(orderId)).thenReturn(response);

            ResponseEntity<CustomerOrderResponse> result = orderController.getOrderById(orderId);

            assertEquals(HttpStatus.OK, result.getStatusCode());
            assertEquals(response, result.getBody());
        }
    }

    @Nested
    @DisplayName("getOrdersByCustomer")
    class GetOrdersByCustomer {

        @Test
        @DisplayName("should return 200 and list of orders when customer has orders")
        void shouldReturn200AndListOfOrdersWhenCustomerHasOrders() {
            Long customerId = 1L;
            List<CustomerOrderResponse> responses = List.of(new CustomerOrderResponse());

            when(customerOrderService.getCustomerOrdersByCustomer(customerId)).thenReturn(responses);

            ResponseEntity<List<CustomerOrderResponse>> result = orderController.getOrdersByCustomer(customerId);

            assertEquals(HttpStatus.OK, result.getStatusCode());
            assertEquals(responses, result.getBody());
        }
    }

    @Nested
    @DisplayName("getOrdersByRestaurant")
    class GetOrdersByRestaurant {

        @Test
        @DisplayName("should return 200 and list of orders when restaurant has orders")
        void shouldReturn200AndListOfOrdersWhenRestaurantHasOrders() {
            Long restaurantId = 1L;
            List<CustomerOrderResponse> responses = List.of(new CustomerOrderResponse());

            when(customerOrderService.getCustomerOrdersByRestaurant(restaurantId)).thenReturn(responses);

            ResponseEntity<List<CustomerOrderResponse>> result = orderController.getOrdersByRestaurant(restaurantId);

            assertEquals(HttpStatus.OK, result.getStatusCode());
            assertEquals(responses, result.getBody());
        }
    }

    @Nested
    @DisplayName("updateOrderStatus")
    class UpdateCustomerOrderStatus {

        @Test
        @DisplayName("should return 200 and updated order when status is updated")
        void shouldReturn200AndUpdatedOrderWhenStatusIsUpdated() {
            Long orderId = 1L;
            OrderStatusUpdateRequest request = new OrderStatusUpdateRequest();
            CustomerOrderResponse response = new CustomerOrderResponse();

            when(customerOrderService.updateCustomerOrderStatus(orderId, request)).thenReturn(response);

            ResponseEntity<CustomerOrderResponse> result = orderController.updateOrderStatus(orderId, request);

            assertEquals(HttpStatus.OK, result.getStatusCode());
            assertEquals(response, result.getBody());
        }
    }

    @Nested
    @DisplayName("cancelOrder")
    class CancelOrder {

        @Test
        @DisplayName("should return 204 when order is cancelled")
        void shouldReturn204WhenOrderIsCancelled() {
            Long orderId = 1L;

            doNothing().when(customerOrderService).cancelCustomerOrder(orderId);

            ResponseEntity<Void> result = orderController.cancelOrder(orderId);

            assertEquals(HttpStatus.NO_CONTENT, result.getStatusCode());
            assertNull(result.getBody());
        }
    }
}
