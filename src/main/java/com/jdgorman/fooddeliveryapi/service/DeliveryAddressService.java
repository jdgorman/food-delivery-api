package com.jdgorman.fooddeliveryapi.service;

import com.jdgorman.fooddeliveryapi.dto.DeliveryAddressRequest;
import com.jdgorman.fooddeliveryapi.dto.DeliveryAddressResponse;
import com.jdgorman.fooddeliveryapi.entity.Customer;
import com.jdgorman.fooddeliveryapi.entity.DeliveryAddress;
import com.jdgorman.fooddeliveryapi.exception.ResourceNotFoundException;
import com.jdgorman.fooddeliveryapi.repository.CustomerRepository;
import com.jdgorman.fooddeliveryapi.repository.DeliveryAddressRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Service that contains business logic for managing delivery addresses for customers.
 * <p>
 * Responsibilities include creating, updating, deleting and retrieving delivery addresses,
 * ensuring addresses are owned by the specified customer and managing the "default" flag
 * behavior (only one default address per customer).
 */
@Service
@RequiredArgsConstructor
public class DeliveryAddressService {

    private final DeliveryAddressRepository addressRepository;
    private final CustomerRepository customerRepository;

    /**
     * Retrieve all delivery addresses associated with a customer.
     *
     * @param customerId the id of the customer whose addresses should be returned
     * @return a list of {@link DeliveryAddressResponse} DTOs for the customer's addresses; may be empty
     * @throws ResourceNotFoundException if the customer with {@code customerId} does not exist
     */
    public List<DeliveryAddressResponse> getDeliveryAddressesByCustomer(Long customerId) {
        // Verify customer exists
        customerRepository.findById(customerId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Customer with id " + customerId + " does not exist"
                ));

        List<DeliveryAddress> addresses = addressRepository.findByCustomerId(customerId);
        return addresses.stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    /**
     * Retrieve a single delivery address by id, validating that it belongs to the given customer.
     *
     * @param customerId the id of the customer who should own the address
     * @param addressId  the id of the address to retrieve
     * @return a {@link DeliveryAddressResponse} DTO for the requested address
     * @throws ResourceNotFoundException if the address does not exist or does not belong to the customer
     */
    public DeliveryAddressResponse getDeliveryAddressById(Long customerId, Long addressId) {
        DeliveryAddress address = addressRepository.findById(addressId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Delivery address with id " + addressId + " does not exist"
                ));

        // Validate address belongs to customer
        if (!address.getCustomer().getId().equals(customerId)) {
            throw new ResourceNotFoundException(
                    "Delivery address with id " + addressId + " does not belong to customer " + customerId
            );
        }

        return convertToResponse(address);
    }

    /**
     * Create a new delivery address for a customer.
     * <p>
     * If the incoming request marks the address as default, existing default addresses for the
     * customer will be unset before persisting the new address.
     *
     * @param customerId the id of the customer to associate the new address with
     * @param request    the {@link DeliveryAddressRequest} payload containing address fields
     * @return the created {@link DeliveryAddressResponse} DTO
     * @throws ResourceNotFoundException if the customer does not exist
     */
    @Transactional
    public DeliveryAddressResponse createDeliveryAddress(Long customerId, DeliveryAddressRequest request) {
        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Customer with id " + customerId + " does not exist"
                ));

        // If this is marked as default, unset any existing default
        if (request.getIsDefault()) {
            unsetDefaultDeliveryAddresses(customerId);
        }

        DeliveryAddress address = DeliveryAddress.builder()
                .customer(customer)
                .label(request.getLabel())
                .streetAddress(request.getStreetAddress())
                .city(request.getCity())
                .state(request.getState())
                .zipCode(request.getZipCode())
                .isDefault(request.getIsDefault())
                .build();

        DeliveryAddress saved = addressRepository.save(address);
        return convertToResponse(saved);
    }

    /**
     * Update an existing delivery address for a customer.
     * <p>
     * Validates ownership, handles default-flag transitions, updates fields and persists the change.
     *
     * @param customerId the id of the customer who must own the address
     * @param addressId  the id of the address to update
     * @param request     the {@link DeliveryAddressRequest} payload containing updated values
     * @return the updated {@link DeliveryAddressResponse} DTO
     * @throws ResourceNotFoundException if the address does not exist or does not belong to the customer
     */
    @Transactional
    public DeliveryAddressResponse updateDeliveryAddress(Long customerId, Long addressId, DeliveryAddressRequest request) {
        DeliveryAddress address = addressRepository.findById(addressId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Cannot update. Delivery address with id " + addressId + " does not exist"
                ));

        // Validate address belongs to customer
        if (!address.getCustomer().getId().equals(customerId)) {
            throw new ResourceNotFoundException(
                    "Delivery address with id " + addressId + " does not belong to customer " + customerId
            );
        }

        // If changing to default, unset any existing default
        if (request.getIsDefault() && !address.getIsDefault()) {
            unsetDefaultDeliveryAddresses(customerId);
        }

        address.setLabel(request.getLabel());
        address.setStreetAddress(request.getStreetAddress());
        address.setCity(request.getCity());
        address.setState(request.getState());
        address.setZipCode(request.getZipCode());
        address.setIsDefault(request.getIsDefault());
        address.setUpdateTimestamp(LocalDateTime.now());

        DeliveryAddress updated = addressRepository.save(address);
        return convertToResponse(updated);
    }

    /**
     * Delete a delivery address after validating it belongs to the given customer.
     *
     * @param customerId the id of the customer who should own the address
     * @param addressId  the id of the address to delete
     * @throws ResourceNotFoundException if the address does not exist or does not belong to the customer
     */
    @Transactional
    public void deleteDeliveryAddress(Long customerId, Long addressId) {
        DeliveryAddress address = addressRepository.findById(addressId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Cannot delete. Delivery address with id " + addressId + " does not exist"
                ));

        // Validate address belongs to customer
        if (!address.getCustomer().getId().equals(customerId)) {
            throw new ResourceNotFoundException(
                    "Delivery address with id " + addressId + " does not belong to customer " + customerId
            );
        }

        addressRepository.deleteById(addressId);
    }

    /**
     * Unset the 'isDefault' flag on any addresses for the given customer.
     *
     * @param customerId the id of the customer whose default addresses should be cleared
     */
    private void unsetDefaultDeliveryAddresses(Long customerId) {
        List<DeliveryAddress> defaultAddresses = addressRepository.findByCustomerIdAndIsDefaultTrue(customerId);
        defaultAddresses.forEach(addr -> {
            addr.setIsDefault(false);
            addressRepository.save(addr);
        });
    }

    /**
     * Convert a {@link DeliveryAddress} entity to a {@link DeliveryAddressResponse} DTO.
     *
     * @param address the entity to convert; must not be null
     * @return a populated {@link DeliveryAddressResponse}
     */
    private DeliveryAddressResponse convertToResponse(DeliveryAddress address) {
        return DeliveryAddressResponse.builder()
                .id(address.getId())
                .customerId(address.getCustomer().getId())
                .customerName(address.getCustomer().getFirstName() + " " + address.getCustomer().getLastName())
                .label(address.getLabel())
                .streetAddress(address.getStreetAddress())
                .city(address.getCity())
                .state(address.getState())
                .zipCode(address.getZipCode())
                .isDefault(address.getIsDefault())
                .createdAt(address.getCreateTimestamp())
                .updatedAt(address.getUpdateTimestamp())
                .build();
    }
}