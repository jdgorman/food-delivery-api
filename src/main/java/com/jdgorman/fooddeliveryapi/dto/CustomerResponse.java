package com.jdgorman.fooddeliveryapi.dto;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class CustomerResponse {

    private Long id;
    private String firstName;
    private String lastName;
    private String email;
    private String phone;
    private Integer addressCount;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}