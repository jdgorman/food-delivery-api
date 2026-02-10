package com.jdgorman.fooddeliveryapi.controller;

import com.jdgorman.fooddeliveryapi.dto.CustomerRequest;
import com.jdgorman.fooddeliveryapi.dto.CustomerResponse;
import com.jdgorman.fooddeliveryapi.service.CustomerService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

class CustomerControllerTest {

    @Mock
    private CustomerService customerService;

    @InjectMocks
    private CustomerController customerController;

    private AutoCloseable closeable;

    @BeforeEach
    void setUp() {
        closeable = MockitoAnnotations.openMocks(this);
    }

    @AfterEach
    void tearDown() throws Exception {
        if (closeable != null) closeable.close();
    }

    @Test
    void getAllCustomersReturnsListOfCustomers() {
        List<CustomerResponse> mockCustomers = Arrays.asList(
                CustomerResponse.builder().id(1L).firstName("John").lastName("Doe").email("john@example.com").build(),
                CustomerResponse.builder().id(2L).firstName("Jane").lastName("Doe").email("jane@example.com").build()
        );
        when(customerService.getAllCustomers()).thenReturn(mockCustomers);

        var response = customerController.getAllCustomers();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(mockCustomers, response.getBody());
    }

    @Test
    void getCustomerByIdReturnsCustomerWhenExists() {
        CustomerResponse mockCustomer = CustomerResponse.builder().id(1L).firstName("John").lastName("Doe").email("john@example.com").build();
        when(customerService.getCustomerById(1L)).thenReturn(mockCustomer);

        var response = customerController.getCustomerById(1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(mockCustomer, response.getBody());
    }

    @Test
    void createCustomerReturnsCreatedCustomer() {
        CustomerRequest request = CustomerRequest.builder().firstName("John").lastName("Doe").email("john@example.com").phone("4172239900").build();
        CustomerResponse mockCustomer = CustomerResponse.builder().id(1L).firstName("John").lastName("Doe").email("john@example.com").build();
        when(customerService.createCustomer(any(CustomerRequest.class))).thenReturn(mockCustomer);

        var response = customerController.createCustomer(request);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(mockCustomer, response.getBody());
    }

    @Test
    void updateCustomerReturnsUpdatedCustomer() {
        CustomerRequest request = CustomerRequest.builder().firstName("John").lastName("Doe").email("john@example.com").phone("4172239900").build();
        CustomerResponse mockCustomer = CustomerResponse.builder().id(1L).firstName("John").lastName("Doe").email("john@example.com").build();
        when(customerService.updateCustomer(eq(1L), any(CustomerRequest.class))).thenReturn(mockCustomer);

        var response = customerController.updateCustomer(1L, request);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(mockCustomer, response.getBody());
    }

    @Test
    void deleteCustomerReturnsNoContent() {
        doNothing().when(customerService).deleteCustomer(1L);

        var response = customerController.deleteCustomer(1L);

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        verify(customerService, times(1)).deleteCustomer(1L);
    }
}
