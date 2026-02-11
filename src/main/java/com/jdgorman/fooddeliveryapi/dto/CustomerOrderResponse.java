package com.jdgorman.fooddeliveryapi.dto;


import com.jdgorman.fooddeliveryapi.enumerator.CustomerOrderStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CustomerOrderResponse {

    private Long id;
    private Long customerId;
    private String customerName;
    private Long restaurantId;
    private String restaurantName;
    private Long deliveryAddressId;
    private String deliveryAddressLabel;
    private String fullDeliveryAddress;
    private CustomerOrderStatus status;
    private List<OrderItemResponse> items;
    private BigDecimal subtotal;
    private BigDecimal tax;
    private BigDecimal deliveryFee;
    private BigDecimal total;
    private String specialInstructions;
    private LocalDateTime createTimestamp;
    private LocalDateTime updateTimestamp;
}