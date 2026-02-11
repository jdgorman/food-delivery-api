package com.jdgorman.fooddeliveryapi.repository;

import com.jdgorman.fooddeliveryapi.entity.Customer;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;


import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;

@DataJpaTest
class CustomerRepositoryTest {

    @Autowired
    private CustomerRepository customerRepository;

    @Test
    void existsByEmailReturnsTrueWhenEmailExists() {
        Customer customer = Customer.builder()
                .firstName("John")
                .lastName("Doe")
                .email("test@example.com")
                .phone("123-456-7890")
                .build();
        customerRepository.save(customer);

        boolean exists = customerRepository.existsByEmail("test@example.com");

        assertTrue(exists);
    }

    @Test
    void existsByEmailReturnsFalseWhenEmailDoesNotExist() {
        boolean exists = customerRepository.existsByEmail("nonexistent@example.com");

        assertFalse(exists);
    }

    @Test
    void findByEmailReturnsCustomerWhenEmailExists() {
        Customer customer = Customer.builder()
                .firstName("Jane")
                .lastName("Smith")
                .email("test@example.com")
                .phone("123-456-7890")
                .build();
        customerRepository.save(customer);

        Optional<Customer> foundCustomer = customerRepository.findByEmail("test@example.com");

        assertTrue(foundCustomer.isPresent());
        assertEquals("test@example.com", foundCustomer.get().getEmail());
    }

    @Test
    void findByEmailReturnsEmptyWhenEmailDoesNotExist() {
        Optional<Customer> foundCustomer = customerRepository.findByEmail("nonexistent@example.com");

        assertFalse(foundCustomer.isPresent());
    }
}
