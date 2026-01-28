package com.jdgorman.fooddeliveryapi.exception;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class DuplicateResourceExceptionTest {

    @Test
    void exceptionMessageIsSetCorrectly() {
        String message = "Resource already exists";
        DuplicateResourceException exception = new DuplicateResourceException(message);

        assertEquals(message, exception.getMessage());
    }

    @Test
    void exceptionIsThrownWithCorrectMessage() {
        String message = "Duplicate entry found";

        DuplicateResourceException exception = assertThrows(
                DuplicateResourceException.class,
                () -> { throw new DuplicateResourceException(message); }
        );

        assertEquals(message, exception.getMessage());
    }
}
