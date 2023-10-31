package com.example.recyclingplastic.exceptions;

public class UserProfileNotFoundException extends UserAlreadyExistsException{
    public UserProfileNotFoundException(String message) {
        super(message);
    }
}
