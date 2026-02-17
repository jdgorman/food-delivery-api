package com.jdgorman.fooddeliveryapi.dto;

import com.jdgorman.fooddeliveryapi.enumerator.CustomerOrderStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DeliveryResponse {

    private Long id;
    private Long orderId;
    private String customerName;
    private String restaurantName;
    private String deliveryAddress;
    private Long driverId;
    private String driverName;
    private String driverPhone;
    private String driverVehicle;
    private CustomerOrderStatus status;
    private LocalDateTime pickupTimestamp;
    private LocalDateTime deliveryTimestamp;
    private LocalDateTime estimatedDeliveryTime;
    private LocalDateTime createTimestamp;
    private LocalDateTime updateTimestamp;
}