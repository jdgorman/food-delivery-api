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

@Service
@RequiredArgsConstructor
public class DeliveryAddressService {

    private final DeliveryAddressRepository addressRepository;
    private final CustomerRepository customerRepository;

    public List<DeliveryAddressResponse> getAddressesByCustomer(Long customerId) {
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

    public DeliveryAddressResponse getAddressById(Long customerId, Long addressId) {
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

    @Transactional
    public DeliveryAddressResponse createAddress(Long customerId, DeliveryAddressRequest request) {
        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Customer with id " + customerId + " does not exist"
                ));

        // If this is marked as default, unset any existing default
        if (request.getIsDefault()) {
            unsetDefaultAddresses(customerId);
        }

        DeliveryAddress address = new DeliveryAddress();
        address.setCustomer(customer);
        address.setLabel(request.getLabel());
        address.setStreetAddress(request.getStreetAddress());
        address.setCity(request.getCity());
        address.setState(request.getState());
        address.setZipCode(request.getZipCode());
        address.setIsDefault(request.getIsDefault());

        DeliveryAddress saved = addressRepository.save(address);
        return convertToResponse(saved);
    }

    @Transactional
    public DeliveryAddressResponse updateAddress(Long customerId, Long addressId, DeliveryAddressRequest request) {
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
            unsetDefaultAddresses(customerId);
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

    @Transactional
    public void deleteAddress(Long customerId, Long addressId) {
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

    private void unsetDefaultAddresses(Long customerId) {
        List<DeliveryAddress> defaultAddresses = addressRepository.findByCustomerIdAndIsDefaultTrue(customerId);
        defaultAddresses.forEach(addr -> {
            addr.setIsDefault(false);
            addressRepository.save(addr);
        });
    }

    private DeliveryAddressResponse convertToResponse(DeliveryAddress address) {
        DeliveryAddressResponse response = new DeliveryAddressResponse();
        response.setId(address.getId());
        response.setCustomerId(address.getCustomer().getId());
        response.setCustomerName(address.getCustomer().getFirstName() + " " + address.getCustomer().getLastName());
        response.setLabel(address.getLabel());
        response.setStreetAddress(address.getStreetAddress());
        response.setCity(address.getCity());
        response.setState(address.getState());
        response.setZipCode(address.getZipCode());
        response.setIsDefault(address.getIsDefault());
        response.setCreatedAt(address.getCreateTimestamp());
        response.setUpdatedAt(address.getUpdateTimestamp());
        return response;
    }
}