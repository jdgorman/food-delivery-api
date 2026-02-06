package com.jdgorman.fooddeliveryapi.exception;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class InvalidEnumValueExceptionTest {

    @Test
    void exceptionMessageIsSetCorrectly() {
        String message = "Invalid enum value";
        String[] validValues = {"VALUE1", "VALUE2"};
        InvalidEnumValueException exception = new InvalidEnumValueException(message, validValues);

        assertEquals(message, exception.getMessage());
    }

    @Test
    void validValuesAreSetCorrectly() {
        String message = "Invalid enum value";
        String[] validValues = {"VALUE1", "VALUE2"};
        InvalidEnumValueException exception = new InvalidEnumValueException(message, validValues);

        assertArrayEquals(validValues, exception.getValidValues());
    }

    @Test
    void exceptionIsThrownWithCorrectMessageAndValidValues() {
        String message = "Invalid enum value";
        String[] validValues = {"VALUE1", "VALUE2"};

        InvalidEnumValueException exception = assertThrows(
                InvalidEnumValueException.class,
                () -> { throw new InvalidEnumValueException(message, validValues); }
        );

        assertEquals(message, exception.getMessage());
        assertArrayEquals(validValues, exception.getValidValues());
    }
}
