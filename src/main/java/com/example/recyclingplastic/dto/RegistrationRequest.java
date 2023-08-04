package com.example.recyclingplastic.dto;



public record RegistrationRequest(
        String firstName,
        String lastName,
        String email,
        String password,
        String role) {

}

