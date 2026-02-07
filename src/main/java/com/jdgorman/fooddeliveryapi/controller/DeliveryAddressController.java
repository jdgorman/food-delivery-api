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

    @GetMapping
    public ResponseEntity<List<DeliveryAddressResponse>> getAddressesByCustomer(
            @PathVariable Long customerId) {
        List<DeliveryAddressResponse> addresses = addressService.getAddressesByCustomer(customerId);
        return ResponseEntity.ok(addresses);
    }

    @GetMapping("/{addressId}")
    public ResponseEntity<DeliveryAddressResponse> getAddressById(
            @PathVariable Long customerId,
            @PathVariable Long addressId) {
        DeliveryAddressResponse address = addressService.getAddressById(customerId, addressId);
        return ResponseEntity.ok(address);
    }

    @PostMapping
    public ResponseEntity<DeliveryAddressResponse> createAddress(
            @PathVariable Long customerId,
            @Valid @RequestBody DeliveryAddressRequest request) {
        DeliveryAddressResponse created = addressService.createAddress(customerId, request);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @PutMapping("/{addressId}")
    public ResponseEntity<DeliveryAddressResponse> updateAddress(
            @PathVariable Long customerId,
            @PathVariable Long addressId,
            @Valid @RequestBody DeliveryAddressRequest request) {
        DeliveryAddressResponse updated = addressService.updateAddress(customerId, addressId, request);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{addressId}")
    public ResponseEntity<Void> deleteAddress(
            @PathVariable Long customerId,
            @PathVariable Long addressId) {
        addressService.deleteAddress(customerId, addressId);
        return ResponseEntity.noContent().build();
    }
}