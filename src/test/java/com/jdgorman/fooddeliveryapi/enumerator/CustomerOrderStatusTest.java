package com.jdgorman.fooddeliveryapi.enumerator;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CustomerOrderStatusTest {

    @Test
    @DisplayName("should contain all expected statuses")
    void shouldContainAllExpectedStatuses() {
        assertEquals(7, CustomerOrderStatus.values().length);
        assertTrue(CustomerOrderStatus.valueOf("PENDING") != null);
        assertTrue(CustomerOrderStatus.valueOf("CONFIRMED") != null);
        assertTrue(CustomerOrderStatus.valueOf("PREPARING") != null);
        assertTrue(CustomerOrderStatus.valueOf("READY") != null);
        assertTrue(CustomerOrderStatus.valueOf("OUT_FOR_DELIVERY") != null);
        assertTrue(CustomerOrderStatus.valueOf("DELIVERED") != null);
        assertTrue(CustomerOrderStatus.valueOf("CANCELLED") != null);
    }

    @Test
    @DisplayName("should return correct ordinal values for statuses")
    void shouldReturnCorrectOrdinalValuesForStatuses() {
        assertEquals(0, CustomerOrderStatus.PENDING.ordinal());
        assertEquals(1, CustomerOrderStatus.CONFIRMED.ordinal());
        assertEquals(2, CustomerOrderStatus.PREPARING.ordinal());
        assertEquals(3, CustomerOrderStatus.READY.ordinal());
        assertEquals(4, CustomerOrderStatus.OUT_FOR_DELIVERY.ordinal());
        assertEquals(5, CustomerOrderStatus.DELIVERED.ordinal());
        assertEquals(6, CustomerOrderStatus.CANCELLED.ordinal());
    }

    @Test
    @DisplayName("should throw IllegalArgumentException for invalid status")
    void shouldThrowIllegalArgumentExceptionForInvalidStatus() {
        assertThrows(IllegalArgumentException.class, () -> CustomerOrderStatus.valueOf("INVALID"));
    }
}
