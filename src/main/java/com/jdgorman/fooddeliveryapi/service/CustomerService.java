package com.jdgorman.fooddeliveryapi.service;

import com.jdgorman.fooddeliveryapi.dto.CustomerRequest;
import com.jdgorman.fooddeliveryapi.dto.CustomerResponse;
import com.jdgorman.fooddeliveryapi.entity.Customer;
import com.jdgorman.fooddeliveryapi.exception.DuplicateResourceException;
import com.jdgorman.fooddeliveryapi.exception.ResourceNotFoundException;
import com.jdgorman.fooddeliveryapi.repository.CustomerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Service that encapsulates business logic for managing customers.
 * <p>
 * Provides methods to create, read, update and delete customers. Methods return
 * {@link CustomerResponse} DTOs for use by controllers; the repository deals with the
 * {@link Customer} entity.
 */
@Service
@RequiredArgsConstructor
public class CustomerService {

    private final CustomerRepository customerRepository;

    /**
     * Retrieve all customers.
     *
     * @return list of {@link CustomerResponse} objects; empty list if no customers exist
     */
    public List<CustomerResponse> getAllCustomers() {
        List<Customer> customers = customerRepository.findAll();
        return customers.stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    /**
     * Retrieve a single customer by id.
     *
     * @param id the id of the customer to fetch
     * @return {@link CustomerResponse} for the requested customer
     */
    public CustomerResponse getCustomerById(Long id) {
        Customer customer = customerRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Customer with id " + id + " does not exist"
                ));
        return convertToResponse(customer);
    }

    /**
     * Create a new customer.
     *
     * Validates that the supplied email is not already in use. The method persists a new
     * {@link Customer} entity and returns a {@link CustomerResponse} representing the saved entity.
     *
     * @param request the {@link CustomerRequest} payload containing firstName, lastName, email and phone
     * @return {@link CustomerResponse} representing the created customer
     */
    @Transactional
    public CustomerResponse createCustomer(CustomerRequest request) {
        if (customerRepository.existsByEmail(request.getEmail())) {
            throw new DuplicateResourceException(
                    "Customer with email '" + request.getEmail() + "' already exists"
            );
        }

        Customer customer = Customer.builder()
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .email(request.getEmail())
                .phone(request.getPhone())
                .build();

        Customer saved = customerRepository.save(customer);
        return convertToResponse(saved);
    }

    /**
     * Update an existing customer.
     *
     * Ensures the customer exists and that any attempted change to the email does not
     * conflict with another existing customer. The updated {@link CustomerResponse} is returned.
     *
     * @param id      id of the customer to update
     * @param request {@link CustomerRequest} containing fields to update
     * @return {@link CustomerResponse} representing the updated customer
     */
    @Transactional
    public CustomerResponse updateCustomer(Long id, CustomerRequest request) {
        Customer customer = customerRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Cannot update. Customer with id " + id + " does not exist"
                ));

        // Check if email is being changed to one that already exists
        if (!customer.getEmail().equals(request.getEmail()) &&
                customerRepository.existsByEmail(request.getEmail())) {
            throw new DuplicateResourceException(
                    "Customer with email '" + request.getEmail() + "' already exists"
            );
        }

        customer.setFirstName(request.getFirstName());
        customer.setLastName(request.getLastName());
        customer.setEmail(request.getEmail());
        customer.setPhone(request.getPhone());

        Customer updated = customerRepository.save(customer);
        return convertToResponse(updated);
    }

    /**
     * Delete a customer by id.
     *
     * @param id id of the customer to delete
     */
    @Transactional
    public void deleteCustomer(Long id) {
        if (!customerRepository.existsById(id)) {
            throw new ResourceNotFoundException(
                    "Cannot delete. Customer with id " + id + " does not exist"
            );
        }
        customerRepository.deleteById(id);
    }

    /**
     * Convert a {@link Customer} entity to a {@link CustomerResponse} DTO.
     *
     * @param customer the entity to convert; must not be null
     * @return populated {@link CustomerResponse}
     */
    private CustomerResponse convertToResponse(Customer customer) {
        return CustomerResponse.builder()
                .id(customer.getId())
                .firstName(customer.getFirstName())
                .lastName(customer.getLastName())
                .email(customer.getEmail())
                .phone(customer.getPhone())
                .addressCount(customer.getAddresses() != null ? customer.getAddresses().size() : 0)
                .createTimestamp(customer.getCreateTimestamp())
                .updateTimestamp(customer.getUpdateTimestamp())
                .build();
    }
}