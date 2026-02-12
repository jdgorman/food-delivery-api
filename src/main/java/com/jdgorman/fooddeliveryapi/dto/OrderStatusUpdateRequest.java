package com.jdgorman.fooddeliveryapi.dto;

import com.jdgorman.fooddeliveryapi.enumerator.CustomerOrderStatus;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderStatusUpdateRequest {

    @NotNull(message = "Order status is required")
    private CustomerOrderStatus status;
}