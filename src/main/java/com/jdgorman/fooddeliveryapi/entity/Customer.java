package com.jdgorman.fooddeliveryapi.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "customer",
        uniqueConstraints = @UniqueConstraint(
                columnNames = "email",
                name = "uk_customer_email"
        ))
@Data
public class Customer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "First name is required")
    @Column(nullable = false)
    private String firstName;

    @NotBlank(message = "Last name is required")
    @Column(nullable = false)
    private String lastName;

    @NotBlank(message = "Email is required")
    @Email(message = "Email must be valid")
    @Column(nullable = false, unique = true)
    private String email;

    @NotBlank(message = "Phone number is required")
    @Column(nullable = false)
    private String phone;

    @OneToMany(mappedBy = "customer", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<DeliveryAddress> addresses = new ArrayList<>();

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
}