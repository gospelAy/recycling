package com.example.recyclingplastic.exceptions;

public class AdminNotFoundException extends CustomerNotFoundException{
    public AdminNotFoundException(String message) {
        super(message);
    }
}
