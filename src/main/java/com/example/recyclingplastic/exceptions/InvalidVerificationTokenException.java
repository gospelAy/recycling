package com.example.recyclingplastic.exceptions;

public class InvalidVerificationTokenException extends RuntimeException{
    public InvalidVerificationTokenException(String message) {
        super(message);
    }
}
