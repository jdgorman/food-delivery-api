package com.jdgorman.fooddeliveryapi.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Entity
@Table(name = "delivery_address")
@Data
@Builder
@JsonIgnoreProperties({"hibernateLazyInitializer","handler"})
@NoArgsConstructor
@AllArgsConstructor
public class DeliveryAddress {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_id", nullable = false)
    private Customer customer;

    @NotBlank(message = "Address label is required")
    @Column(nullable = false)
    private String label;

    @NotBlank(message = "Street address is required")
    @Column(nullable = false)
    private String streetAddress;

    @NotBlank(message = "City is required")
    @Column(nullable = false)
    private String city;

    @NotBlank(message = "State is required")
    @Column(nullable = false)
    private String state;

    @NotBlank(message = "Zip code is required")
    @Column(nullable = false)
    private String zipCode;

    @Builder.Default
    @Column(nullable = false)
    private Boolean isDefault = false;

    @Column(nullable = false, updatable = false)
    private LocalDateTime createTimestamp;

    @Column(nullable = false)
    private LocalDateTime updateTimestamp;

    @PrePersist
    void onCreate() {
        LocalDateTime now = LocalDateTime.now();
        this.createTimestamp = now;
        this.updateTimestamp = now;
    }

    @PreUpdate
    protected void onUpdate() {
        this.updateTimestamp = LocalDateTime.now();
    }
}