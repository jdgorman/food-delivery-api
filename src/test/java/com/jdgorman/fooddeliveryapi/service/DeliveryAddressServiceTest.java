package com.jdgorman.fooddeliveryapi.service;

import com.jdgorman.fooddeliveryapi.dto.DeliveryAddressRequest;
import com.jdgorman.fooddeliveryapi.dto.DeliveryAddressResponse;
import com.jdgorman.fooddeliveryapi.entity.Customer;
import com.jdgorman.fooddeliveryapi.entity.DeliveryAddress;
import com.jdgorman.fooddeliveryapi.exception.ResourceNotFoundException;
import com.jdgorman.fooddeliveryapi.repository.CustomerRepository;
import com.jdgorman.fooddeliveryapi.repository.DeliveryAddressRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class DeliveryAddressServiceTest {

    @Mock
    private DeliveryAddressRepository deliveryAddressRepository;

    @Mock
    private CustomerRepository customerRepository;

    @InjectMocks
    private DeliveryAddressService deliveryAddressService;

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
    void createDeliveryAddressSavesAndReturnsAddress() {
        Long customerId = 1L;
        Customer customer = Customer.builder().id(customerId).firstName("John").lastName("Doe").build();

        DeliveryAddressRequest request = DeliveryAddressRequest.builder()
                .label("Home")
                .streetAddress("123 Main St")
                .city("Anytown")
                .state("CA")
                .zipCode("12345")
                .isDefault(true)
                .build();

        DeliveryAddress savedAddress = DeliveryAddress.builder()
                .id(1L)
                .customer(customer)
                .label(request.getLabel())
                .streetAddress(request.getStreetAddress())
                .city(request.getCity())
                .state(request.getState())
                .zipCode(request.getZipCode())
                .isDefault(true)
                .build();

        when(customerRepository.findById(customerId)).thenReturn(Optional.of(customer));
        when(deliveryAddressRepository.save(any(DeliveryAddress.class))).thenReturn(savedAddress);

        DeliveryAddressResponse response = deliveryAddressService.createDeliveryAddress(customerId, request);

        assertEquals(savedAddress.getId(), response.getId());
        assertEquals(savedAddress.getCustomer().getId(), response.getCustomerId());
        assertEquals(savedAddress.getLabel(), response.getLabel());
        assertEquals(savedAddress.getStreetAddress(), response.getStreetAddress());
        assertEquals(savedAddress.getCity(), response.getCity());
        assertEquals(savedAddress.getState(), response.getState());
        assertEquals(savedAddress.getZipCode(), response.getZipCode());
    }

    @Test
    void getDeliveryAddressByIdReturnsAddressWhenExistsAndBelongsToCustomer() {
        Long customerId = 1L;
        Long addressId = 1L;
        Customer customer = Customer.builder().id(customerId).firstName("John").lastName("Doe").build();

        DeliveryAddress address = DeliveryAddress.builder()
                .id(addressId)
                .customer(customer)
                .label("Home")
                .streetAddress("123 Main St")
                .city("Anytown")
                .state("CA")
                .zipCode("12345")
                .build();

        when(deliveryAddressRepository.findById(addressId)).thenReturn(Optional.of(address));

        DeliveryAddressResponse response = deliveryAddressService.getDeliveryAddressById(customerId, addressId);

        assertEquals(address.getId(), response.getId());
        assertEquals(address.getCustomer().getId(), response.getCustomerId());
        assertEquals(address.getLabel(), response.getLabel());
        assertEquals(address.getStreetAddress(), response.getStreetAddress());
        assertEquals(address.getCity(), response.getCity());
        assertEquals(address.getState(), response.getState());
        assertEquals(address.getZipCode(), response.getZipCode());
    }

    @Test
    void getDeliveryAddressByIdThrowsExceptionWhenNotFound() {
        when(deliveryAddressRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> deliveryAddressService.getDeliveryAddressById(1L, 1L));
    }

    @Test
    void deleteDeliveryAddressDeletesWhenExistsAndBelongsToCustomer() {
        Long customerId = 1L;
        Long addressId = 1L;
        Customer customer = Customer.builder().id(customerId).firstName("John").lastName("Doe").build();

        DeliveryAddress address = DeliveryAddress.builder()
                .id(addressId)
                .customer(customer)
                .label("Home")
                .streetAddress("123 Main St")
                .city("Anytown")
                .state("CA")
                .zipCode("12345")
                .build();

        when(deliveryAddressRepository.findById(addressId)).thenReturn(Optional.of(address));

        deliveryAddressService.deleteDeliveryAddress(customerId, addressId);

        verify(deliveryAddressRepository, times(1)).deleteById(addressId);
    }

    @Test
    void deleteDeliveryAddressThrowsExceptionWhenNotFound() {
        when(deliveryAddressRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> deliveryAddressService.deleteDeliveryAddress(1L, 1L));
    }
}
