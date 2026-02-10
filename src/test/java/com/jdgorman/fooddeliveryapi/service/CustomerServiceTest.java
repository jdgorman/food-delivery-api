package com.jdgorman.fooddeliveryapi.service;

import com.jdgorman.fooddeliveryapi.dto.CustomerRequest;
import com.jdgorman.fooddeliveryapi.dto.CustomerResponse;
import com.jdgorman.fooddeliveryapi.entity.Customer;
import com.jdgorman.fooddeliveryapi.entity.DeliveryAddress;
import com.jdgorman.fooddeliveryapi.exception.ResourceNotFoundException;
import com.jdgorman.fooddeliveryapi.repository.CustomerRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CustomerServiceTest {

    @Mock
    private CustomerRepository customerRepository;

    @InjectMocks
    private CustomerService customerService;

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
    void getCustomerByIdReturnsCustomerWhenExists() {
        List<DeliveryAddress> addresses = List.of(
                DeliveryAddress.builder()
                        .id(1L)
                        .streetAddress("123 Main St")
                        .city("Anytown")
                        .state("CA")
                        .zipCode("12345")
                        .build()
        );

        Customer customer = Customer.builder()
                .id(1L)
                .firstName("John")
                .lastName("Doe")
                .email("john.doe@example.com")
                .phone("555-123-4567")
                .addresses(addresses)
                .build();

        when(customerRepository.findById(1L)).thenReturn(Optional.of(customer));

        CustomerResponse result = customerService.getCustomerById(1L);

        assertEquals("John", result.getFirstName());
        assertEquals("Doe", result.getLastName());
        assertEquals("john.doe@example.com", result.getEmail());
        assertEquals("555-123-4567", result.getPhone());
    }

    @Test
    void getCustomerByIdThrowsExceptionWhenNotFound() {
        when(customerRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> customerService.getCustomerById(1L));
    }

    @Test
    void createCustomerSavesAndReturnsCustomer() {
        List<DeliveryAddress> addresses = List.of(
                DeliveryAddress.builder()
                        .id(1L)
                        .streetAddress("123 Main St")
                        .city("Anytown")
                        .state("CA")
                        .zipCode("12345")
                        .build()
        );

        Customer customer = Customer.builder()
                .firstName("Jane")
                .lastName("Smith")
                .email("jane.smith@example.com")
                .phone("555-987-6543")
                .addresses(addresses)
                .build();

        // Return a saved customer instance (with id) from the mock
        customer.setId(1L);
        when(customerRepository.existsByEmail(anyString())).thenReturn(false);
        when(customerRepository.save(any(Customer.class))).thenReturn(customer);

        CustomerRequest request = CustomerRequest.builder()
                .firstName(customer.getFirstName())
                .lastName(customer.getLastName())
                .email(customer.getEmail())
                .phone(customer.getPhone())
                .build();

        CustomerResponse result = customerService.createCustomer(request);

        assertEquals("Jane", result.getFirstName());
        assertEquals("Smith", result.getLastName());
        assertEquals("jane.smith@example.com", result.getEmail());
        assertEquals("555-987-6543", result.getPhone());
    }
}
