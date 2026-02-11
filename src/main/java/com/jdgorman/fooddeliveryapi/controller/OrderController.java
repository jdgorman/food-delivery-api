package com.jdgorman.fooddeliveryapi.controller;

import com.jdgorman.fooddeliveryapi.dto.CustomerOrderRequest;
import com.jdgorman.fooddeliveryapi.dto.CustomerOrderResponse;
import com.jdgorman.fooddeliveryapi.dto.OrderStatusUpdateRequest;
import com.jdgorman.fooddeliveryapi.service.CustomerOrderService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST controller that exposes endpoints for creating and managing customer orders.
 *
 * <p>Base path: {@code /api/orders}. This controller delegates to {@link CustomerOrderService}
 * and returns {@link CustomerOrderResponse} DTOs.</p>
 */
@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
public class OrderController {

    private final CustomerOrderService customerOrderService;

    /**
     * Create a new order from the request payload.
     *
     * @param request validated order request
     * @return created order response with HTTP 201 status
     */
    @PostMapping
    public ResponseEntity<CustomerOrderResponse> createOrder(@Valid @RequestBody CustomerOrderRequest request) {
        CustomerOrderResponse created = customerOrderService.createCustomerOrder(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    /**
     * Retrieve a single order by id.
     *
     * @param id order id
     * @return order response if found
     */
    @GetMapping("/{id}")
    public ResponseEntity<CustomerOrderResponse> getOrderById(@PathVariable Long id) {
        CustomerOrderResponse order = customerOrderService.getCustomerOrderById(id);
        return ResponseEntity.ok(order);
    }

    /**
     * Retrieve all orders for a customer.
     *
     * @param customerId customer id
     * @return list of orders for the customer
     */
    @GetMapping("/customer/{customerId}")
    public ResponseEntity<List<CustomerOrderResponse>> getOrdersByCustomer(@PathVariable Long customerId) {
        List<CustomerOrderResponse> orders = customerOrderService.getCustomerOrdersByCustomer(customerId);
        return ResponseEntity.ok(orders);
    }

    /**
     * Retrieve all orders for a restaurant.
     *
     * @param restaurantId restaurant id
     * @return list of orders for the restaurant
     */
    @GetMapping("/restaurant/{restaurantId}")
    public ResponseEntity<List<CustomerOrderResponse>> getOrdersByRestaurant(@PathVariable Long restaurantId) {
        List<CustomerOrderResponse> orders = customerOrderService.getCustomerOrdersByRestaurant(restaurantId);
        return ResponseEntity.ok(orders);
    }

    /**
     * Update the status of an existing order.
     *
     * @param id order id
     * @param request validated status update request
     * @return updated order response
     */
    @PutMapping("/{id}/status")
    public ResponseEntity<CustomerOrderResponse> updateOrderStatus(
            @PathVariable Long id,
            @Valid @RequestBody OrderStatusUpdateRequest request) {
        CustomerOrderResponse updated = customerOrderService.updateCustomerOrderStatus(id, request);
        return ResponseEntity.ok(updated);
    }

    /**
     * Cancel an order by id.
     *
     * @param id order id
     * @return HTTP 204 if cancellation succeeds
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> cancelOrder(@PathVariable Long id) {
        customerOrderService.cancelCustomerOrder(id);
        return ResponseEntity.noContent().build();
    }
}