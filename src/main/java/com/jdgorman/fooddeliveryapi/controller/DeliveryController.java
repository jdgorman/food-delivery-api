package com.jdgorman.fooddeliveryapi.controller;

import com.jdgorman.fooddeliveryapi.dto.DeliveryRequest;
import com.jdgorman.fooddeliveryapi.dto.DeliveryResponse;
import com.jdgorman.fooddeliveryapi.service.DeliveryService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST controller for managing deliveries.
 * Provides endpoints for creating, retrieving, and updating delivery information.
 */
@RestController
@RequestMapping("/api/deliveries")
@RequiredArgsConstructor
public class DeliveryController {

    private final DeliveryService deliveryService;

    /**
     * Creates a new delivery.
     *
     * @param request the delivery request containing delivery details
     * @return the created delivery response
     */
    @PostMapping
    public ResponseEntity<DeliveryResponse> createDelivery(@Valid @RequestBody DeliveryRequest request) {
        DeliveryResponse created = deliveryService.createDelivery(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    /**
     * Retrieves a delivery by its ID.
     *
     * @param id the ID of the delivery to retrieve
     * @return the delivery response
     */
    @GetMapping("/{id}")
    public ResponseEntity<DeliveryResponse> getDeliveryById(@PathVariable Long id) {
        DeliveryResponse delivery = deliveryService.getDeliveryById(id);
        return ResponseEntity.ok(delivery);
    }

    /**
     * Retrieves a delivery by the associated order ID.
     *
     * @param orderId the ID of the order associated with the delivery
     * @return the delivery response
     */
    @GetMapping("/order/{orderId}")
    public ResponseEntity<DeliveryResponse> getDeliveryByOrderId(@PathVariable Long orderId) {
        DeliveryResponse delivery = deliveryService.getDeliveryByOrderId(orderId);
        return ResponseEntity.ok(delivery);
    }

    /**
     * Retrieves all deliveries assigned to a specific driver.
     *
     * @param driverId the ID of the driver
     * @return a list of delivery responses
     */
    @GetMapping("/driver/{driverId}")
    public ResponseEntity<List<DeliveryResponse>> getDeliveriesByDriver(@PathVariable Long driverId) {
        List<DeliveryResponse> deliveries = deliveryService.getDeliveriesByDriver(driverId);
        return ResponseEntity.ok(deliveries);
    }

    /**
     * Marks a delivery as picked up.
     *
     * @param id the ID of the delivery to mark as picked up
     * @return the updated delivery response
     */
    @PutMapping("/{id}/pickup")
    public ResponseEntity<DeliveryResponse> markAsPickedUp(@PathVariable Long id) {
        DeliveryResponse updated = deliveryService.markAsPickedUp(id);
        return ResponseEntity.ok(updated);
    }

    /**
     * Marks a delivery as delivered.
     *
     * @param id the ID of the delivery to mark as delivered
     * @return the updated delivery response
     */
    @PutMapping("/{id}/complete")
    public ResponseEntity<DeliveryResponse> markAsDelivered(@PathVariable Long id) {
        DeliveryResponse updated = deliveryService.markAsDelivered(id);
        return ResponseEntity.ok(updated);
    }
}