package com.jdgorman.fooddeliveryapi.enumerator;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class DriverStatusTest {

    @Test
    void shouldContainAvailableStatus() {
        DriverStatus status = DriverStatus.AVAILABLE;
        assertNotNull(status);
        assertEquals("AVAILABLE", status.name());
    }

    @Test
    void shouldContainBusyStatus() {
        DriverStatus status = DriverStatus.BUSY;
        assertNotNull(status);
        assertEquals("BUSY", status.name());
    }

    @Test
    void shouldContainOfflineStatus() {
        DriverStatus status = DriverStatus.OFFLINE;
        assertNotNull(status);
        assertEquals("OFFLINE", status.name());
    }
}
