package com.jdgorman.fooddeliveryapi.service;

import com.jdgorman.fooddeliveryapi.dto.DeliveryRequest;
import com.jdgorman.fooddeliveryapi.dto.DeliveryResponse;
import com.jdgorman.fooddeliveryapi.entity.*;
import com.jdgorman.fooddeliveryapi.enumerator.CustomerOrderStatus;
import com.jdgorman.fooddeliveryapi.enumerator.DriverStatus;
import com.jdgorman.fooddeliveryapi.exception.ResourceNotFoundException;
import com.jdgorman.fooddeliveryapi.repository.DeliveryRepository;
import com.jdgorman.fooddeliveryapi.repository.DriverRepository;
import com.jdgorman.fooddeliveryapi.repository.CustomerOrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Service class for managing deliveries.
 * Handles the business logic for creating, retrieving, and updating delivery information.
 */
@Service
@RequiredArgsConstructor
public class DeliveryService {

    private final DeliveryRepository deliveryRepository;
    private final CustomerOrderRepository orderRepository;
    private final DriverRepository driverRepository;

    /**
     * Creates a new delivery.
     *
     * @param request the delivery request containing delivery details
     * @return the created delivery response
     */
    @Transactional
    public DeliveryResponse createDelivery(DeliveryRequest request) {
        // Validate order exists
        CustomerOrder order = orderRepository.findById(request.getOrderId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Order with id " + request.getOrderId() + " does not exist"
                ));

        // Check if order already has a delivery
        if (deliveryRepository.existsByCustomerOrderId(request.getOrderId())) {
            throw new IllegalStateException(
                    "Order with id " + request.getOrderId() + " already has a delivery assigned"
            );
        }

        // Validate order is in READY status
        if (order.getStatus() != CustomerOrderStatus.READY) {
            throw new IllegalStateException(
                    "Cannot assign delivery. Order must be in READY status. Current status: " + order.getStatus()
            );
        }

        // Validate driver exists
        Driver driver = driverRepository.findById(request.getDriverId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Driver with id " + request.getDriverId() + " does not exist"
                ));

        // Check if driver is available
        if (driver.getStatus() != DriverStatus.AVAILABLE) {
            throw new IllegalStateException(
                    "Driver is not available. Current status: " + driver.getStatus()
            );
        }

        // Create delivery
        Delivery delivery = Delivery.builder()
                .customerOrder(order)
                .driver(driver)
                .status(CustomerOrderStatus.READY)
                .estimatedDeliveryTime(request.getEstimatedDeliveryTime())
                .build();

        // Update driver status to BUSY
        driver.setStatus(DriverStatus.BUSY);
        driverRepository.save(driver);

        Delivery saved = deliveryRepository.save(delivery);
        return convertToResponse(saved);
    }

    /**
     * Retrieves a delivery by its ID.
     *
     * @param id the ID of the delivery to retrieve
     * @return the delivery response
     */
    public DeliveryResponse getDeliveryById(Long id) {
        Delivery delivery = deliveryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Delivery with id " + id + " does not exist"
                ));
        return convertToResponse(delivery);
    }

    /**
     * Retrieves a delivery by the associated order ID.
     *
     * @param orderId the ID of the order associated with the delivery
     * @return the delivery response
     */
    public DeliveryResponse getDeliveryByOrderId(Long orderId) {
        Delivery delivery = deliveryRepository.findByCustomerOrderId(orderId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "No delivery found for order with id " + orderId
                ));
        return convertToResponse(delivery);
    }

    /**
     * Retrieves all deliveries assigned to a specific driver.
     *
     * @param driverId the ID of the driver
     * @return a list of delivery responses
     */
    public List<DeliveryResponse> getDeliveriesByDriver(Long driverId) {
        // Verify driver exists
        driverRepository.findById(driverId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Driver with id " + driverId + " does not exist"
                ));

        List<Delivery> deliveries = deliveryRepository.findByDriverIdOrderByCreateTimestampDesc(driverId);
        return deliveries.stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    /**
     * Marks a delivery as picked up.
     *
     * @param id the ID of the delivery to mark as picked up
     * @return the updated delivery response
     */
    @Transactional
    public DeliveryResponse markAsPickedUp(Long id) {
        Delivery delivery = deliveryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Delivery with id " + id + " does not exist"
                ));

        if (delivery.getStatus() != CustomerOrderStatus.READY) {
            throw new IllegalStateException(
                    "Cannot mark as picked up. Delivery must be in READY status. Current status: " + delivery.getStatus()
            );
        }

        delivery.setPickupTimestamp(LocalDateTime.now());
        delivery.setStatus(CustomerOrderStatus.OUT_FOR_DELIVERY);
        delivery.setUpdateTimestamp(LocalDateTime.now());

        // Update order status
        CustomerOrder order = delivery.getCustomerOrder();
        order.setStatus(CustomerOrderStatus.OUT_FOR_DELIVERY);
        order.setUpdateTimestamp(LocalDateTime.now());
        orderRepository.save(order);

        Delivery updated = deliveryRepository.save(delivery);
        return convertToResponse(updated);
    }

    /**
     * Marks a delivery as delivered.
     *
     * @param id the ID of the delivery to mark as delivered
     * @return the updated delivery response
     */
    @Transactional
    public DeliveryResponse markAsDelivered(Long id) {
        Delivery delivery = deliveryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Delivery with id " + id + " does not exist"
                ));

        if (delivery.getStatus() != CustomerOrderStatus.OUT_FOR_DELIVERY) {
            throw new IllegalStateException(
                    "Cannot mark as delivered. Delivery must be OUT_FOR_DELIVERY. Current status: " + delivery.getStatus()
            );
        }

        delivery.setDeliveryTimestamp(LocalDateTime.now());
        delivery.setStatus(CustomerOrderStatus.DELIVERED);
        delivery.setUpdateTimestamp(LocalDateTime.now());

        // Update order status
        CustomerOrder order = delivery.getCustomerOrder();
        order.setStatus(CustomerOrderStatus.DELIVERED);
        order.setUpdateTimestamp(LocalDateTime.now());
        orderRepository.save(order);

        // Update driver status back to AVAILABLE
        Driver driver = delivery.getDriver();
        driver.setStatus(DriverStatus.AVAILABLE);
        driver.setUpdateTimestamp(LocalDateTime.now());
        driverRepository.save(driver);

        Delivery updated = deliveryRepository.save(delivery);
        return convertToResponse(updated);
    }

    private DeliveryResponse convertToResponse(Delivery delivery) {
        CustomerOrder order = delivery.getCustomerOrder();
        Driver driver = delivery.getDriver();

        String deliveryAddress = String.format("%s, %s, %s %s",
                order.getDeliveryAddress().getStreetAddress(),
                order.getDeliveryAddress().getCity(),
                order.getDeliveryAddress().getState(),
                order.getDeliveryAddress().getZipCode()
        );

        return DeliveryResponse.builder()
                .id(delivery.getId())
                .orderId(order.getId())
                .customerName(order.getCustomer().getFirstName() + " " + order.getCustomer().getLastName())
                .restaurantName(order.getRestaurant().getName())
                .deliveryAddress(deliveryAddress)
                .driverId(driver.getId())
                .driverName(driver.getFirstName() + " " + driver.getLastName())
                .driverPhone(driver.getPhone())
                .driverVehicle(driver.getVehicleType() +
                        (driver.getLicensePlate() != null ? " (" + driver.getLicensePlate() + ")" : ""))
                .status(delivery.getStatus())
                .pickupTimestamp(delivery.getPickupTimestamp())
                .deliveryTimestamp(delivery.getDeliveryTimestamp())
                .estimatedDeliveryTime(delivery.getEstimatedDeliveryTime())
                .createTimestamp(delivery.getCreateTimestamp())
                .updateTimestamp(delivery.getUpdateTimestamp())
                .build();
    }
}