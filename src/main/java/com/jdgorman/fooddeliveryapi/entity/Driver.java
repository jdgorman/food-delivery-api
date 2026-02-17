package com.jdgorman.fooddeliveryapi.entity;

import com.jdgorman.fooddeliveryapi.enumerator.DriverStatus;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "driver",
        uniqueConstraints = @UniqueConstraint(
                columnNames = "email",
                name = "uk_driver_email"
        ))
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Driver {

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

    @NotBlank(message = "Vehicle type is required")
    @Column(nullable = false)
    private String vehicleType;

    private String licensePlate;

    @NotNull(message = "Driver status is required")
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @Builder.Default
    private DriverStatus status = DriverStatus.AVAILABLE;

    @OneToMany(mappedBy = "driver", cascade = CascadeType.ALL)
    @Builder.Default
    private List<Delivery> deliveries = new ArrayList<>();

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