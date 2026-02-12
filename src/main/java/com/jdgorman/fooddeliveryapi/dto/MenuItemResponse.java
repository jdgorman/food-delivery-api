package com.jdgorman.fooddeliveryapi.dto;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.jdgorman.fooddeliveryapi.enumerator.MenuCategory;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@JsonPropertyOrder({ "id", "name", "description", "price", "category", "isAvailable", "restaurantId", "restaurantName",
"createTimestamp", "updateTimestamp" })
@Builder
public class MenuItemResponse {

    private Long id;
    private String name;
    private String description;
    private BigDecimal price;
    private MenuCategory category;
    private Boolean isAvailable;
    private Long restaurantId;
    private String restaurantName;
    private LocalDateTime createTimestamp;
    private LocalDateTime updateTimestamp;
}