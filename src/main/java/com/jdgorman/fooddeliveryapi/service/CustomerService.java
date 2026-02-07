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

@Service
@RequiredArgsConstructor
public class CustomerService {

    private final CustomerRepository customerRepository;

    public List<CustomerResponse> getAllCustomers() {
        List<Customer> customers = customerRepository.findAll();
        return customers.stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    public CustomerResponse getCustomerById(Long id) {
        Customer customer = customerRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Customer with id " + id + " does not exist"
                ));
        return convertToResponse(customer);
    }

    @Transactional
    public CustomerResponse createCustomer(CustomerRequest request) {
        if (customerRepository.existsByEmail(request.getEmail())) {
            throw new DuplicateResourceException(
                    "Customer with email '" + request.getEmail() + "' already exists"
            );
        }

        Customer customer = new Customer();
        customer.setFirstName(request.getFirstName());
        customer.setLastName(request.getLastName());
        customer.setEmail(request.getEmail());
        customer.setPhone(request.getPhone());

        Customer saved = customerRepository.save(customer);
        return convertToResponse(saved);
    }

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
        customer.setUpdateTimestamp(LocalDateTime.now());

        Customer updated = customerRepository.save(customer);
        return convertToResponse(updated);
    }

    @Transactional
    public void deleteCustomer(Long id) {
        if (!customerRepository.existsById(id)) {
            throw new ResourceNotFoundException(
                    "Cannot delete. Customer with id " + id + " does not exist"
            );
        }
        customerRepository.deleteById(id);
    }

    private CustomerResponse convertToResponse(Customer customer) {
        CustomerResponse response = new CustomerResponse();
        response.setId(customer.getId());
        response.setFirstName(customer.getFirstName());
        response.setLastName(customer.getLastName());
        response.setEmail(customer.getEmail());
        response.setPhone(customer.getPhone());
        response.setAddressCount(customer.getAddresses().size());
        response.setCreatedAt(customer.getCreateTimestamp());
        response.setUpdatedAt(customer.getUpdateTimestamp());
        return response;
    }
}