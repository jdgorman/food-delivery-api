package com.jdgorman.fooddeliveryapi.dto;

import com.jdgorman.fooddeliveryapi.entity.MenuCategory;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
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