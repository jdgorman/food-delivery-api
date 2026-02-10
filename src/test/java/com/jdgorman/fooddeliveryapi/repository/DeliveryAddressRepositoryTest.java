package com.jdgorman.fooddeliveryapi.repository;

import com.jdgorman.fooddeliveryapi.entity.Customer;
import com.jdgorman.fooddeliveryapi.entity.DeliveryAddress;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;


import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DataJpaTest
class DeliveryAddressRepositoryTest {

    @Autowired
    private DeliveryAddressRepository deliveryAddressRepository;

    @Autowired
    private CustomerRepository customerRepository;

    @Test
    void findByCustomerIdReturnsAddressesForCustomer() {
        Customer customer = Customer.builder()
                .firstName("John")
                .lastName("Doe")
                .email("john@example.com")
                .phone("123-456-7890")
                .build();
        customer = customerRepository.save(customer);

        DeliveryAddress address1 = DeliveryAddress.builder()
                .customer(customer)
                .label("Home")
                .streetAddress("123 Main St")
                .city("Dallas")
                .state("TX")
                .zipCode("75201")
                .isDefault(false)
                .build();
        DeliveryAddress address2 = DeliveryAddress.builder()
                .customer(customer)
                .label("Work")
                .streetAddress("456 Elm St")
                .city("Dallas")
                .state("TX")
                .zipCode("73301")
                .isDefault(true)
                .build();
        deliveryAddressRepository.save(address1);
        deliveryAddressRepository.save(address2);

        List<DeliveryAddress> addresses = deliveryAddressRepository.findByCustomerId(customer.getId());

        assertEquals(2, addresses.size());
    }

    @Test
    void findByCustomerIdAndIsDefaultTrueReturnsDefaultAddressForCustomer() {
        Customer customer = Customer.builder()
                .firstName("John")
                .lastName("Doe")
                .email("john@example.com")
                .phone("123-456-7890")
                .build();
        customer = customerRepository.save(customer);

        DeliveryAddress address1 = DeliveryAddress.builder()
                .customer(customer)
                .label("Home")
                .streetAddress("123 Main St")
                .city("Dallas")
                .state("TX")
                .zipCode("75201")
                .isDefault(false)
                .build();
        DeliveryAddress address2 = DeliveryAddress.builder()
                .customer(customer)
                .label("Work")
                .streetAddress("456 Elm St")
                .city("Dallas")
                .state("TX")
                .zipCode("73301")
                .isDefault(true)
                .build();
        deliveryAddressRepository.save(address1);
        deliveryAddressRepository.save(address2);

        List<DeliveryAddress> defaultAddresses = deliveryAddressRepository.findByCustomerIdAndIsDefaultTrue(customer.getId());

        assertEquals(1, defaultAddresses.size());
        assertTrue(defaultAddresses.get(0).getIsDefault());
    }
}
