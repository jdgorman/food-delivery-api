package com.jdgorman.fooddeliveryapi.exception;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ErrorResponseTest {

    @Test
    void errorResponseFieldsAreSetCorrectly() {
        LocalDateTime timestamp = LocalDateTime.now();
        int status = 404;
        String error = "Not Found";
        String message = "Resource not found";
        String path = "/api/resource";

        ErrorResponse errorResponse = new ErrorResponse(timestamp, status, error, message, path);

        assertEquals(timestamp, errorResponse.getTimestamp());
        assertEquals(status, errorResponse.getStatus());
        assertEquals(error, errorResponse.getError());
        assertEquals(message, errorResponse.getMessage());
        assertEquals(path, errorResponse.getPath());
    }

    @Test
    void errorResponseHandlesNullValues() {
        ErrorResponse errorResponse = new ErrorResponse(null, 500, null, null, null);

        assertEquals(null, errorResponse.getTimestamp());
        assertEquals(500, errorResponse.getStatus());
        assertEquals(null, errorResponse.getError());
        assertEquals(null, errorResponse.getMessage());
        assertEquals(null, errorResponse.getPath());
    }
}
