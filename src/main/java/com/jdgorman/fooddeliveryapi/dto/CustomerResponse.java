package com.jdgorman.fooddeliveryapi.dto;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.Builder;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@JsonPropertyOrder({ "id", "firstName", "lastName", "email", "phone", "addressCount", "createTimestamp", "updateTimestamp"})
@Builder
public class CustomerResponse {

    private Long id;
    private String firstName;
    private String lastName;
    private String email;
    private String phone;
    private Integer addressCount;
    private LocalDateTime createTimestamp;
    private LocalDateTime updateTimestamp;
}