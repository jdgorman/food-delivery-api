package com.jdgorman.fooddeliveryapi.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CustomerRequest {

    @NotBlank(message = "First name is required")
    private String firstName;

    @NotBlank(message = "Last name is required")
    private String lastName;

    @NotBlank(message = "Email is required")
    @Email(message = "Email must be valid")
    private String email;

    @NotBlank(message = "Phone number is required")
    @Pattern(regexp = "^(?:\\+1\\s?)?(?:\\(\\d{3}\\)|\\d{3})[.\\-\\s]?\\d{3}[.\\-\\s]?\\d{4}$",
            message = "Phone must be a valid US phone number")
    private String phone;
}