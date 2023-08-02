package com.example.recyclingplastic.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Customer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long customerId;
    private String first_name;
    private String last_name;
    private String email;
    private String password;
    @Enumerated(EnumType.STRING)
    private Role role;
    private String username;
}
