package com.jdgorman.fooddeliveryapi.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Entity
@Table(name = "order_items")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "Order is required")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id", nullable = false)
    private CustomerOrder order;

    @NotNull(message = "Menu item is required")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "menu_item_id", nullable = false)
    private MenuItem menuItem;

    @NotNull(message = "Quantity is required")
    @Min(value = 1, message = "Quantity must be at least 1")
    @Column(nullable = false)
    private Integer quantity;

    @NotNull(message = "Price is required")
    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal priceAtOrder;

    @NotNull(message = "Subtotal is required")
    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal subtotal;
}