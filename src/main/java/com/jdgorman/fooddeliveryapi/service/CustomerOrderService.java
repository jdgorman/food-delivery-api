package com.jdgorman.fooddeliveryapi.service;

import com.jdgorman.fooddeliveryapi.dto.*;
import com.jdgorman.fooddeliveryapi.entity.*;
import com.jdgorman.fooddeliveryapi.enumerator.CustomerOrderStatus;
import com.jdgorman.fooddeliveryapi.exception.ResourceNotFoundException;
import com.jdgorman.fooddeliveryapi.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Service that encapsulates business logic for creating and managing customer orders.
 *
 * <p>Handles validation of related entities, pricing calculations, and status transitions.
 * Methods return {@link CustomerOrderResponse} DTOs for controller use.</p>
 */
@Service
@RequiredArgsConstructor
public class CustomerOrderService {

    private final CustomerOrderRepository customerOrderRepository;
    private final CustomerRepository customerRepository;
    private final RestaurantRepository restaurantRepository;
    private final DeliveryAddressRepository deliveryAddressRepository;
    private final MenuItemRepository menuItemRepository;

    private static final BigDecimal TAX_RATE = new BigDecimal("0.0825"); // 8.25%
    private static final BigDecimal DELIVERY_FEE = new BigDecimal("5.00");

    /**
     * Create a new order and calculate pricing totals.
     *
     * @param request order request payload
     * @return created order response
     */
    @Transactional
    public CustomerOrderResponse createCustomerOrder(CustomerOrderRequest request) {
        // Validate customer exists
        Customer customer = customerRepository.findById(request.getCustomerId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Customer with id " + request.getCustomerId() + " does not exist"
                ));

        // Validate restaurant exists
        Restaurant restaurant = restaurantRepository.findById(request.getRestaurantId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Restaurant with id " + request.getRestaurantId() + " does not exist"
                ));

        // Validate delivery address exists and belongs to customer
        DeliveryAddress deliveryAddress = deliveryAddressRepository.findById(request.getDeliveryAddressId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Delivery address with id " + request.getDeliveryAddressId() + " does not exist"
                ));

        if (!deliveryAddress.getCustomer().getId().equals(request.getCustomerId())) {
            throw new ResourceNotFoundException(
                    "Delivery address with id " + request.getDeliveryAddressId() +
                            " does not belong to customer " + request.getCustomerId()
            );
        }

        // Fetch all menu items in a single query to avoid N+1
        List<Long> menuItemIds = request.getItems().stream()
                .map(OrderItemRequest::getMenuItemId)
                .collect(Collectors.toList());

        List<MenuItem> menuItems = menuItemRepository.findAllById(menuItemIds);

        // Validate all requested menu items were found
        if (menuItems.size() != menuItemIds.size()) {
            List<Long> foundIds = menuItems.stream()
                    .map(MenuItem::getId)
                    .collect(Collectors.toList());
            List<Long> missingIds = menuItemIds.stream()
                    .filter(id -> !foundIds.contains(id))
                    .collect(Collectors.toList());
            throw new ResourceNotFoundException(
                    "Menu items with ids " + missingIds + " do not exist"
            );
        }

        // Create a map for efficient lookup
        var menuItemMap = menuItems.stream()
                .collect(Collectors.toMap(MenuItem::getId, item -> item));

        // Validate all menu items belong to the restaurant and are available
        for (MenuItem menuItem : menuItems) {
            if (!menuItem.getRestaurant().getId().equals(request.getRestaurantId())) {
                throw new ResourceNotFoundException(
                        "Menu item with id " + menuItem.getId() +
                                " does not belong to restaurant " + request.getRestaurantId()
                );
            }

            if (!menuItem.getIsAvailable()) {
                throw new IllegalStateException(
                        "Menu item '" + menuItem.getName() + "' is currently unavailable"
                );
            }
        }

        // Create order
        CustomerOrder order = CustomerOrder.builder()
                .customer(customer)
                .restaurant(restaurant)
                .deliveryAddress(deliveryAddress)
                .status(CustomerOrderStatus.PENDING)
                .specialInstructions(request.getSpecialInstructions())
                .build();

        // Calculate subtotal and create order items
        BigDecimal subtotal = BigDecimal.ZERO;

        for (OrderItemRequest itemRequest : request.getItems()) {
            MenuItem menuItem = menuItemMap.get(itemRequest.getMenuItemId());

            BigDecimal itemSubtotal = menuItem.getPrice()
                    .multiply(new BigDecimal(itemRequest.getQuantity()))
                    .setScale(2, RoundingMode.HALF_UP);

            OrderItem orderItem = OrderItem.builder()
                    .order(order)
                    .menuItem(menuItem)
                    .quantity(itemRequest.getQuantity())
                    .priceAtOrder(menuItem.getPrice())
                    .subtotal(itemSubtotal)
                    .build();

            order.getOrderItems().add(orderItem);
            subtotal = subtotal.add(itemSubtotal);
        }

        // Calculate tax and total
        BigDecimal tax = subtotal.multiply(TAX_RATE).setScale(2, RoundingMode.HALF_UP);
        BigDecimal total = subtotal.add(tax).add(DELIVERY_FEE);

        order.setSubtotal(subtotal);
        order.setTax(tax);
        order.setDeliveryFee(DELIVERY_FEE);
        order.setTotal(total);

        CustomerOrder savedOrder = customerOrderRepository.save(order);
        return convertToResponse(savedOrder);
    }

    /**
     * Retrieve a single order by id.
     *
     * @param id order id
     * @return order response
     */
    public CustomerOrderResponse getCustomerOrderById(Long id) {
        CustomerOrder order = customerOrderRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Order with id " + id + " does not exist"
                ));
        return convertToResponse(order);
    }

    /**
     * Retrieve all orders for a customer, most recent first.
     *
     * @param customerId customer id
     * @return list of order responses
     */
    public List<CustomerOrderResponse> getCustomerOrdersByCustomer(Long customerId) {
        // Verify customer exists
        customerRepository.findById(customerId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Customer with id " + customerId + " does not exist"
                ));

        List<CustomerOrder> orders = customerOrderRepository.findByCustomerIdOrderByCreateTimestampDesc(customerId);
        return orders.stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    /**
     * Retrieve all orders for a restaurant, most recent first.
     *
     * @param restaurantId restaurant id
     * @return list of order responses
     */
    public List<CustomerOrderResponse> getCustomerOrdersByRestaurant(Long restaurantId) {
        // Verify restaurant exists
        restaurantRepository.findById(restaurantId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Restaurant with id " + restaurantId + " does not exist"
                ));

        List<CustomerOrder> orders = customerOrderRepository.findByRestaurantIdOrderByCreateTimestampDesc(restaurantId);
        return orders.stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    /**
     * Update the status of an order with validation of transition rules.
     *
     * @param id order id
     * @param request status update request
     * @return updated order response
     */
    @Transactional
    public CustomerOrderResponse updateCustomerOrderStatus(Long id, OrderStatusUpdateRequest request) {
        CustomerOrder order = customerOrderRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Order with id " + id + " does not exist"
                ));

        // Validate status transition
        validateStatusTransition(order.getStatus(), request.getStatus());

        order.setStatus(request.getStatus());
        order.setUpdateTimestamp(LocalDateTime.now());

        CustomerOrder updated = customerOrderRepository.save(order);
        return convertToResponse(updated);
    }

    /**
     * Cancel an order if it is still in a cancellable state.
     *
     * @param id order id
     */
    @Transactional
    public void cancelCustomerOrder(Long id) {
        CustomerOrder order = customerOrderRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Cannot cancel. Order with id " + id + " does not exist"
                ));

        // Only allow cancellation if order is PENDING or CONFIRMED
        if (order.getStatus() != CustomerOrderStatus.PENDING && order.getStatus() != CustomerOrderStatus.CONFIRMED) {
            throw new IllegalStateException(
                    "Cannot cancel order. Order is already " + order.getStatus()
            );
        }

        order.setStatus(CustomerOrderStatus.CANCELLED);
        order.setUpdateTimestamp(LocalDateTime.now());
        customerOrderRepository.save(order);
    }

    /**
     * Validate status transitions according to the order workflow rules.
     *
     * @param currentStatus current order status
     * @param newStatus requested status
     */
    private void validateStatusTransition(CustomerOrderStatus currentStatus, CustomerOrderStatus newStatus) {
        // Can't change from terminal states
        if (currentStatus == CustomerOrderStatus.DELIVERED || currentStatus == CustomerOrderStatus.CANCELLED) {
            throw new IllegalStateException(
                    "Cannot update order status from " + currentStatus
            );
        }

        // Cancellation is only allowed from PENDING or CONFIRMED
        if (newStatus == CustomerOrderStatus.CANCELLED) {
            if (currentStatus != CustomerOrderStatus.PENDING && currentStatus != CustomerOrderStatus.CONFIRMED) {
                throw new IllegalStateException(
                        "Cannot cancel order. Order is already " + currentStatus
                );
            }
            return; // Valid cancellation, no further checks needed
        }

        // Can't go backwards in the workflow
        int currentOrder = getStatusOfCustomerOrder(currentStatus);
        int newOrder = getStatusOfCustomerOrder(newStatus);

        if (newOrder < currentOrder) {
            throw new IllegalStateException(
                    "Cannot change status from " + currentStatus + " to " + newStatus
            );
        }
    }

    /**
     * Convert a status to an ordered step value for transition checks.
     *
     * @param status order status
     * @return numeric position in the workflow
     */
    private int getStatusOfCustomerOrder(CustomerOrderStatus status) {
        return switch (status) {
            case PENDING -> 1;
            case CONFIRMED -> 2;
            case PREPARING -> 3;
            case READY -> 4;
            case OUT_FOR_DELIVERY -> 5;
            case DELIVERED -> 6;
            case CANCELLED -> 7;
        };
    }

    /**
     * Map a {@link com.jdgorman.fooddeliveryapi.entity.CustomerOrder} entity to a response DTO.
     *
     * @param order order entity
     * @return response DTO
     */
    private CustomerOrderResponse convertToResponse(CustomerOrder order) {
        List<OrderItemResponse> itemResponses = order.getOrderItems().stream()
                .map(item -> OrderItemResponse.builder()
                        .id(item.getId())
                        .menuItemId(item.getMenuItem().getId())
                        .menuItemName(item.getMenuItem().getName())
                        .quantity(item.getQuantity())
                        .priceAtOrder(item.getPriceAtOrder())
                        .subtotal(item.getSubtotal())
                        .build())
                .collect(Collectors.toList());

        String fullAddress = String.format("%s, %s, %s %s",
                order.getDeliveryAddress().getStreetAddress(),
                order.getDeliveryAddress().getCity(),
                order.getDeliveryAddress().getState(),
                order.getDeliveryAddress().getZipCode()
        );

        return CustomerOrderResponse.builder()
                .id(order.getId())
                .customerId(order.getCustomer().getId())
                .customerName(order.getCustomer().getFirstName() + " " + order.getCustomer().getLastName())
                .restaurantId(order.getRestaurant().getId())
                .restaurantName(order.getRestaurant().getName())
                .deliveryAddressId(order.getDeliveryAddress().getId())
                .deliveryAddressLabel(order.getDeliveryAddress().getLabel())
                .fullDeliveryAddress(fullAddress)
                .status(order.getStatus())
                .items(itemResponses)
                .subtotal(order.getSubtotal())
                .tax(order.getTax())
                .deliveryFee(order.getDeliveryFee())
                .total(order.getTotal())
                .specialInstructions(order.getSpecialInstructions())
                .createTimestamp(order.getCreateTimestamp())
                .updateTimestamp(order.getUpdateTimestamp())
                .build();
    }
}

