package com.jdgorman.fooddeliveryapi.controller;

import com.jdgorman.fooddeliveryapi.dto.CustomerRequest;
import com.jdgorman.fooddeliveryapi.dto.CustomerResponse;
import com.jdgorman.fooddeliveryapi.service.CustomerService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST controller that exposes endpoints to manage customers.
 * <p>
 * Base path: <code>/api/customers</code>
 * <p>
 * This controller delegates business logic to {@link CustomerService} and maps requests/responses
 * to DTOs: {@link CustomerRequest} and {@link CustomerResponse}.
 */
@RestController
@RequestMapping("/api/customers")
@RequiredArgsConstructor
public class CustomerController {

    private final CustomerService customerService;

    /**
     * Retrieve all customers.
     *
     * @return HTTP 200 OK with a list of {@link CustomerResponse} objects. The list may be empty if no customers exist.
     */
    @GetMapping
    public ResponseEntity<List<CustomerResponse>> getAllCustomers() {
        List<CustomerResponse> customers = customerService.getAllCustomers();
        return ResponseEntity.ok(customers);
    }

    /**
     * Retrieve a single customer by id.
     *
     * @param id the id of the customer to retrieve
     * @return HTTP 200 OK with the {@link CustomerResponse} if found
     * @throws com.jdgorman.fooddeliveryapi.exception.ResourceNotFoundException if no customer with the given id exists
     */
    @GetMapping("/{id}")
    public ResponseEntity<CustomerResponse> getCustomerById(@PathVariable Long id) {
        CustomerResponse customer = customerService.getCustomerById(id);
        return ResponseEntity.ok(customer);
    }

    /**
     * Create a new customer.
     *
     * @param request the {@link CustomerRequest} payload; validated using Bean Validation annotations
     * @return HTTP 201 Created with the created {@link CustomerResponse}
     * @throws com.jdgorman.fooddeliveryapi.exception.DuplicateResourceException if a customer with the same email already exists
     * @throws jakarta.validation.ConstraintViolationException if the request payload fails validation
     */
    @PostMapping
    public ResponseEntity<CustomerResponse> createCustomer(@Valid @RequestBody CustomerRequest request) {
        CustomerResponse created = customerService.createCustomer(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    /**
     * Update an existing customer.
     *
     * @param id      the id of the customer to update
     * @param request the {@link CustomerRequest} payload containing updated values; validated using Bean Validation
     * @return HTTP 200 OK with the updated {@link CustomerResponse}
     * @throws com.jdgorman.fooddeliveryapi.exception.ResourceNotFoundException if the customer does not exist
     * @throws com.jdgorman.fooddeliveryapi.exception.DuplicateResourceException if the updated email conflicts with another customer
     * @throws jakarta.validation.ConstraintViolationException if the request payload fails validation
     */
    @PutMapping("/{id}")
    public ResponseEntity<CustomerResponse> updateCustomer(
            @PathVariable Long id,
            @Valid @RequestBody CustomerRequest request) {
        CustomerResponse updated = customerService.updateCustomer(id, request);
        return ResponseEntity.ok(updated);
    }

    /**
     * Delete a customer by id.
     *
     * @param id the id of the customer to delete
     * @return HTTP 204 No Content when the delete succeeds
     * @throws com.jdgorman.fooddeliveryapi.exception.ResourceNotFoundException if the customer does not exist
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCustomer(@PathVariable Long id) {
        customerService.deleteCustomer(id);
        return ResponseEntity.noContent().build();
    }
}