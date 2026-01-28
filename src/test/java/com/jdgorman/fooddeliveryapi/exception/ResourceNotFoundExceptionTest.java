package com.jdgorman.fooddeliveryapi.exception;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class ResourceNotFoundExceptionTest {

    @Test
    void exceptionMessageIsSetCorrectlyWithResourceFieldAndValue() {
        String resource = "Restaurant";
        String field = "id";
        Long value = 123L;

        ResourceNotFoundException exception = new ResourceNotFoundException(resource, field, value);

        assertEquals("Restaurant not found with id = 123", exception.getMessage());
    }

    @Test
    void exceptionMessageIsSetCorrectlyWithCustomMessage() {
        String message = "Custom not found message";

        ResourceNotFoundException exception = new ResourceNotFoundException(message);

        assertEquals(message, exception.getMessage());
    }

    @Test
    void exceptionMessageAndCauseAreSetCorrectly() {
        String message = "Custom not found message";
        Throwable cause = new IllegalArgumentException("Invalid argument");

        ResourceNotFoundException exception = new ResourceNotFoundException(message, cause);

        assertEquals(message, exception.getMessage());
        assertEquals(cause, exception.getCause());
    }

    @Test
    void exceptionIsThrownWithCorrectMessageAndCause() {
        String message = "Resource not found";
        Throwable cause = new NullPointerException("Null value");

        ResourceNotFoundException exception = assertThrows(
                ResourceNotFoundException.class,
                () -> { throw new ResourceNotFoundException(message, cause); }
        );

        assertEquals(message, exception.getMessage());
        assertEquals(cause, exception.getCause());
    }
}
