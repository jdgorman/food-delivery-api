package com.jdgorman.fooddeliveryapi.enumerator;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CustomerOrderStatusTest {

    @Test
    @DisplayName("should throw IllegalArgumentException for invalid status")
    void shouldThrowIllegalArgumentExceptionForInvalidStatus() {
        assertThrows(IllegalArgumentException.class, () -> CustomerOrderStatus.valueOf("INVALID"));
    }
}
