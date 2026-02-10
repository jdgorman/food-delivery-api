package com.jdgorman.fooddeliveryapi.controller;

import com.jdgorman.fooddeliveryapi.dto.DeliveryAddressRequest;
import com.jdgorman.fooddeliveryapi.dto.DeliveryAddressResponse;
import com.jdgorman.fooddeliveryapi.service.DeliveryAddressService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/customers/{customerId}/addresses")
@RequiredArgsConstructor
public class DeliveryAddressController {

    private final DeliveryAddressService addressService;

    /**
     * Retrieve all delivery addresses for a given customer.
     *
     * @param customerId the id of the customer whose addresses should be returned
     * @return HTTP 200 with a list of DeliveryAddressResponse objects (possibly empty)
     * @throws com.jdgorman.fooddeliveryapi.exception.ResourceNotFoundException if the customer does not exist
     */
    @GetMapping
    public ResponseEntity<List<DeliveryAddressResponse>> getAddressesByCustomer(
            @PathVariable Long customerId) {
        List<DeliveryAddressResponse> addresses = addressService.getDeliveryAddressesByCustomer(customerId);
        return ResponseEntity.ok(addresses);
    }

    /**
     * Retrieve a specific delivery address for a customer by address id.
     *
     * @param customerId the id of the customer
     * @param addressId  the id of the delivery address to retrieve
     * @return HTTP 200 with the DeliveryAddressResponse
     * @throws com.jdgorman.fooddeliveryapi.exception.ResourceNotFoundException if the address does not exist or does not belong to the customer
     */
    @GetMapping("/{addressId}")
    public ResponseEntity<DeliveryAddressResponse> getAddressById(
            @PathVariable Long customerId,
            @PathVariable Long addressId) {
        DeliveryAddressResponse address = addressService.getDeliveryAddressById(customerId, addressId);
        return ResponseEntity.ok(address);
    }

    /**
     * Create a new delivery address for the specified customer.
     *
     * @param customerId the id of the customer to associate the new address with
     * @param request    the payload containing address fields; validated via Bean Validation
     * @return HTTP 201 with the created DeliveryAddressResponse
     * @throws com.jdgorman.fooddeliveryapi.exception.ResourceNotFoundException if the customer does not exist
     * @throws jakarta.validation.ConstraintViolationException if the request fails validation
     */
    @PostMapping
    public ResponseEntity<DeliveryAddressResponse> createAddress(
            @PathVariable Long customerId,
            @Valid @RequestBody DeliveryAddressRequest request) {
        DeliveryAddressResponse created = addressService.createDeliveryAddress(customerId, request);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    /**
     * Update an existing delivery address for the specified customer.
     *
     * @param customerId the id of the customer who owns the address
     * @param addressId  the id of the address to update
     * @param request    the payload with updated address fields; validated via Bean Validation
     * @return HTTP 200 with the updated DeliveryAddressResponse
     * @throws com.jdgorman.fooddeliveryapi.exception.ResourceNotFoundException if the address does not exist or does not belong to the customer
     * @throws jakarta.validation.ConstraintViolationException if the request fails validation
     */
    @PutMapping("/{addressId}")
    public ResponseEntity<DeliveryAddressResponse> updateAddress(
            @PathVariable Long customerId,
            @PathVariable Long addressId,
            @Valid @RequestBody DeliveryAddressRequest request) {
        DeliveryAddressResponse updated = addressService.updateDeliveryAddress(customerId, addressId, request);
        return ResponseEntity.ok(updated);
    }

    /**
     * Delete a delivery address belonging to the specified customer.
     *
     * @param customerId the id of the customer who owns the address
     * @param addressId  the id of the address to delete
     * @return HTTP 204 No Content on success
     * @throws com.jdgorman.fooddeliveryapi.exception.ResourceNotFoundException if the address does not exist or does not belong to the customer
     */
    @DeleteMapping("/{addressId}")
    public ResponseEntity<Void> deleteAddress(
            @PathVariable Long customerId,
            @PathVariable Long addressId) {
        addressService.deleteDeliveryAddress(customerId, addressId);
        return ResponseEntity.noContent().build();
    }
}