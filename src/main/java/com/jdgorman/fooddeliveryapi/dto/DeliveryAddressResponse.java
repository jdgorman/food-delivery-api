package com.jdgorman.fooddeliveryapi.dto;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class DeliveryAddressResponse {

    private Long id;
    private Long customerId;
    private String customerName;
    private String label;
    private String streetAddress;
    private String city;
    private String state;
    private String zipCode;
    private Boolean isDefault;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}