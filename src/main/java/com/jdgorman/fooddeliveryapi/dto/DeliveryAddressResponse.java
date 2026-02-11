package com.jdgorman.fooddeliveryapi.dto;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.Builder;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@JsonPropertyOrder({ "id", "customerId", "customerName", "label", "streetAddress", "city", "state", "zipCode", "isDefault",
        "createdAt", "updatedAt" })
@Builder
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
    private LocalDateTime createTimestamp;
    private LocalDateTime updateTimestamp;
}