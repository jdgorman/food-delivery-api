package com.jdgorman.fooddeliveryapi.entity;

import com.jdgorman.fooddeliveryapi.enumerator.CustomerOrderStatus;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class CustomerOrderTest {

    @Test
    @DisplayName("should build CustomerOrder with default values")
    void shouldBuildCustomerOrderWithDefaultValues() {
        LocalDateTime now = LocalDateTime.now();

        CustomerOrder order = CustomerOrder.builder()
                .id(1L)
                .subtotal(BigDecimal.valueOf(100))
                .tax(BigDecimal.valueOf(8.25))
                .deliveryFee(BigDecimal.valueOf(5))
                .total(BigDecimal.valueOf(113.25))
                .specialInstructions("Leave at the door")
                .createTimestamp(now)
                .updateTimestamp(now)
                .build();

        assertNotNull(order);
        assertEquals(1L, order.getId());
        assertEquals(CustomerOrderStatus.PENDING, order.getStatus());
        assertNotNull(order.getOrderItems());
        assertTrue(order.getOrderItems().isEmpty());
        assertNotNull(order.getCreateTimestamp());
        assertNotNull(order.getUpdateTimestamp());
    }

    @Test
    @DisplayName("should add order item to CustomerOrder")
    void shouldAddOrderItemToCustomerOrder() {
        CustomerOrder order = CustomerOrder.builder().build();
        OrderItem item = OrderItem.builder().id(1L).build();

        order.getOrderItems().add(item);

        assertEquals(1, order.getOrderItems().size());
        assertTrue(order.getOrderItems().contains(item));
    }

    @Test
    @DisplayName("should remove order item from CustomerOrder")
    void shouldRemoveOrderItemFromCustomerOrder() {
        OrderItem item = OrderItem.builder().id(1L).build();
        CustomerOrder order = CustomerOrder.builder()
                .orderItems(new ArrayList<>(List.of(item)))
                .build();

        order.getOrderItems().remove(item);

        assertTrue(order.getOrderItems().isEmpty());
    }

    @Test
    @DisplayName("should update timestamps on CustomerOrder")
    void shouldUpdateTimestampsOnCustomerOrder() {
        LocalDateTime initialTimestamp = LocalDateTime.now();
        CustomerOrder order = CustomerOrder.builder()
                .createTimestamp(initialTimestamp)
                .updateTimestamp(initialTimestamp)
                .build();

        LocalDateTime newTimestamp = LocalDateTime.now().plusHours(1);
        order.setUpdateTimestamp(newTimestamp);

        assertEquals(initialTimestamp, order.getCreateTimestamp());
        assertEquals(newTimestamp, order.getUpdateTimestamp());
    }
}
