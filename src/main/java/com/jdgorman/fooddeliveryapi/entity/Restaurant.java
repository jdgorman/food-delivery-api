package com.jdgorman.fooddeliveryapi.entity;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "restaurant")
@Data
@JsonPropertyOrder({ "id", "name", "address", "phone", "cuisineType", "active", "createDate" })
@JsonIgnoreProperties({"hibernateLazyInitializer","handler"})
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Restaurant {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Restaurant name is required")
    @Column(nullable = false)
    private String name;

    @NotBlank(message = "Address is required")
    private String address;

    @Pattern(regexp = "^(?:\\+1\\s?)?(?:\\(\\d{3}\\)|\\d{3})[.\\-\\s]?\\d{3}[.\\-\\s]?\\d{4}$",
            message = "Phone must be a valid US phone number")
    private String phone;

    @NotBlank(message = "Cuisine type is required")
    private String cuisineType;

    private Boolean active = true;

    @Column(nullable = false, updatable = false)
    private LocalDateTime createTimestamp;

    @Column(nullable = false)
    private LocalDateTime updateTimestamp;

    @PrePersist
    protected void onCreate() {
        LocalDateTime now = LocalDateTime.now();
        this.createTimestamp = now;
        this.updateTimestamp = now;
        if (this.active == null) {
            this.active = true;
        }
    }

    @PreUpdate
    protected void onUpdate() {
        this.updateTimestamp = LocalDateTime.now();
    }
}
