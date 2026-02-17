package com.jdgorman.fooddeliveryapi.dto;

import com.jdgorman.fooddeliveryapi.enumerator.DriverStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DriverResponse {

    private Long id;
    private String firstName;
    private String lastName;
    private String email;
    private String phone;
    private String vehicleType;
    private String licensePlate;
    private DriverStatus status;
    private Integer activeDeliveries;
    private Integer totalDeliveries;
    private LocalDateTime createTimestamp;
    private LocalDateTime updateTimestamp;
}