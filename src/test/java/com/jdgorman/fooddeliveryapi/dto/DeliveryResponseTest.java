package com.jdgorman.fooddeliveryapi.dto;

import com.jdgorman.fooddeliveryapi.enumerator.CustomerOrderStatus;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class DeliveryResponseTest {

    @Test
    void shouldBuildDeliveryResponseWithAllFields() {
        LocalDateTime now = LocalDateTime.now();

        DeliveryResponse response = DeliveryResponse.builder()
                .id(1L)
                .orderId(2L)
                .customerName("John Doe")
                .restaurantName("Pizza Place")
                .deliveryAddress("123 Main St")
                .driverId(3L)
                .driverName("Jane Smith")
                .driverPhone("555-1234")
                .driverVehicle("Car")
                .status(CustomerOrderStatus.PENDING)
                .pickupTimestamp(now.minusMinutes(30))
                .deliveryTimestamp(now.plusMinutes(30))
                .estimatedDeliveryTime(now.plusMinutes(20))
                .createTimestamp(now.minusHours(1))
                .updateTimestamp(now.minusMinutes(10))
                .build();

        assertNotNull(response);
        assertEquals(1L, response.getId());
        assertEquals(2L, response.getOrderId());
        assertEquals("John Doe", response.getCustomerName());
        assertEquals("Pizza Place", response.getRestaurantName());
        assertEquals("123 Main St", response.getDeliveryAddress());
        assertEquals(3L, response.getDriverId());
        assertEquals("Jane Smith", response.getDriverName());
        assertEquals("555-1234", response.getDriverPhone());
        assertEquals("Car", response.getDriverVehicle());
        assertEquals(CustomerOrderStatus.PENDING, response.getStatus());
        assertEquals(now.minusMinutes(30), response.getPickupTimestamp());
        assertEquals(now.plusMinutes(30), response.getDeliveryTimestamp());
        assertEquals(now.plusMinutes(20), response.getEstimatedDeliveryTime());
        assertEquals(now.minusHours(1), response.getCreateTimestamp());
        assertEquals(now.minusMinutes(10), response.getUpdateTimestamp());
    }

    @Test
    void shouldBuildDeliveryResponseWithMinimalFields() {
        DeliveryResponse response = DeliveryResponse.builder()
                .id(1L)
                .orderId(2L)
                .build();

        assertNotNull(response);
        assertEquals(1L, response.getId());
        assertEquals(2L, response.getOrderId());
    }
}
