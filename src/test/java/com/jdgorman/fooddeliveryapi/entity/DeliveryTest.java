package com.jdgorman.fooddeliveryapi.entity;

import com.jdgorman.fooddeliveryapi.enumerator.CustomerOrderStatus;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class DeliveryTest {

    @Test
    void shouldBuildDeliveryWithAllFields() {
        LocalDateTime now = LocalDateTime.now();
        CustomerOrder customerOrder = new CustomerOrder();
        Driver driver = new Driver();

        Delivery delivery = Delivery.builder()
                .id(1L)
                .customerOrder(customerOrder)
                .driver(driver)
                .status(CustomerOrderStatus.READY)
                .pickupTimestamp(now.minusMinutes(30))
                .deliveryTimestamp(now.plusMinutes(30))
                .estimatedDeliveryTime(now.plusMinutes(20))
                .createTimestamp(now.minusHours(1))
                .updateTimestamp(now.minusMinutes(10))
                .build();

        assertNotNull(delivery);
        assertEquals(1L, delivery.getId());
        assertEquals(customerOrder, delivery.getCustomerOrder());
        assertEquals(driver, delivery.getDriver());
        assertEquals(CustomerOrderStatus.READY, delivery.getStatus());
        assertEquals(now.minusMinutes(30), delivery.getPickupTimestamp());
        assertEquals(now.plusMinutes(30), delivery.getDeliveryTimestamp());
        assertEquals(now.plusMinutes(20), delivery.getEstimatedDeliveryTime());
        assertEquals(now.minusHours(1), delivery.getCreateTimestamp());
        assertEquals(now.minusMinutes(10), delivery.getUpdateTimestamp());
    }

    @Test
    void shouldBuildDeliveryWithMinimalFields() {
        CustomerOrder customerOrder = new CustomerOrder();
        Driver driver = new Driver();

        Delivery delivery = Delivery.builder()
                .customerOrder(customerOrder)
                .driver(driver)
                .build();

        assertNotNull(delivery);
        assertEquals(customerOrder, delivery.getCustomerOrder());
        assertEquals(driver, delivery.getDriver());
        assertEquals(CustomerOrderStatus.READY, delivery.getStatus());
    }
}
