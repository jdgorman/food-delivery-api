package com.jdgorman.fooddeliveryapi.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class DeliveryAddressRequest {

    @NotBlank(message = "Address label is required")
    private String label;

    @NotBlank(message = "Street address is required")
    private String streetAddress;

    @NotBlank(message = "City is required")
    private String city;

    @NotBlank(message = "State is required")
    private String state;

    @NotBlank(message = "Zip code is required")
    private String zipCode;

    private Boolean isDefault = false;
}