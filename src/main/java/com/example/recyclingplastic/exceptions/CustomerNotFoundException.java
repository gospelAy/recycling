package com.example.recyclingplastic.exceptions;

public class CustomerNotFoundException extends RuntimeException {
    private static final long serialVerisionUID = 1;

    public CustomerNotFoundException(String message) {
        super(message);
    }
}
