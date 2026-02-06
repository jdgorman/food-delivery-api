package com.jdgorman.fooddeliveryapi.exception;

import org.junit.jupiter.api.Test;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class GlobalExceptionHandlerTest {

    private final GlobalExceptionHandler exceptionHandler = new GlobalExceptionHandler();
    private final WebRequest mockRequest = mock(WebRequest.class);

    @Test
    void handleResourceNotFoundExceptionReturnsNotFoundResponse() {
        when(mockRequest.getDescription(false)).thenReturn("uri=/api/resource");

        ResourceNotFoundException exception = new ResourceNotFoundException("Resource not found");
        ResponseEntity<ErrorResponse> response = exceptionHandler.handleResourceNotFoundException(exception, mockRequest);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("Not Found", response.getBody().getError());
        assertEquals("Resource not found", response.getBody().getMessage());
        assertEquals("/api/resource", response.getBody().getPath());
    }

    @Test
    void handleValidationExceptionReturnsBadRequestResponse() {
        when(mockRequest.getDescription(false)).thenReturn("uri=/api/resource");

        MethodArgumentNotValidException exception = mock(MethodArgumentNotValidException.class);
        org.springframework.validation.BindingResult mockBindingResult = mock(org.springframework.validation.BindingResult.class);
        when(exception.getBindingResult()).thenReturn(mockBindingResult);
        when(mockBindingResult.getFieldErrors()).thenReturn(List.of(
                new org.springframework.validation.FieldError("objectName", "field", "must not be blank")
        ));

        ResponseEntity<ErrorResponse> response = exceptionHandler.handleValidationException(exception, mockRequest);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Validation Failed", response.getBody().getError());
        assertTrue(response.getBody().getMessage().contains("field: must not be blank"));
        assertEquals("/api/resource", response.getBody().getPath());
    }


    @Test
    void handleDuplicateResourceExceptionReturnsConflictResponse() {
        when(mockRequest.getDescription(false)).thenReturn("uri=/api/resource");

        DuplicateResourceException exception = new DuplicateResourceException("Duplicate resource");
        ResponseEntity<ErrorResponse> response = exceptionHandler.handleDuplicateResourceException(exception, mockRequest);

        assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
        assertEquals("Duplicate Resource", response.getBody().getError());
        assertEquals("Duplicate resource", response.getBody().getMessage());
        assertEquals("/api/resource", response.getBody().getPath());
    }

    @Test
    void handleDataIntegrityViolationReturnsConflictResponse() {
        when(mockRequest.getDescription(false)).thenReturn("uri=/api/resource");

        DataIntegrityViolationException exception = new DataIntegrityViolationException("Constraint violation");
        ResponseEntity<ErrorResponse> response = exceptionHandler.handleDataIntegrityViolation(exception, mockRequest);

        assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
        assertEquals("Database Conflict", response.getBody().getError());
        assertEquals("Unable to save restaurant. Duplicate or invalid data.", response.getBody().getMessage());
        assertEquals("/api/resource", response.getBody().getPath());
    }

    @Test
    void handleGlobalExceptionReturnsInternalServerErrorResponse() {
        when(mockRequest.getDescription(false)).thenReturn("uri=/api/resource");

        Exception exception = new Exception("Unexpected error");
        ResponseEntity<ErrorResponse> response = exceptionHandler.handleGlobalException(exception, mockRequest);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertEquals("Internal Server Error", response.getBody().getError());
        assertEquals("Unexpected error", response.getBody().getMessage());
        assertEquals("/api/resource", response.getBody().getPath());
    }

    @Test
    void handleTypeMismatchExceptionReturnsBadRequestResponseForNonEnumParameter() {
        when(mockRequest.getDescription(false)).thenReturn("uri=/api/resource");

        MethodArgumentTypeMismatchException exception = new MethodArgumentTypeMismatchException(
                "value", String.class, "param", null, new IllegalArgumentException("Invalid value")
        );
        ResponseEntity<ErrorResponse> response = exceptionHandler.handleTypeMismatchException(exception, mockRequest);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Invalid Parameter", response.getBody().getError());
        assertTrue(response.getBody().getMessage().contains("Invalid value for parameter 'param'"));
        assertEquals("/api/resource", response.getBody().getPath());
    }
}
