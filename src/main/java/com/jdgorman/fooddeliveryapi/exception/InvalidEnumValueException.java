package com.jdgorman.fooddeliveryapi.exception;

import lombok.Getter;

@Getter
public class InvalidEnumValueException extends RuntimeException {
    private final String[] validValues;

    public InvalidEnumValueException(String message, String[] validValues) {
        super(message);
        this.validValues = validValues;
    }
}