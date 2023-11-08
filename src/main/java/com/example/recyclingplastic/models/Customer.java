package com.example.recyclingplastic.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.NaturalId;

import java.util.Set;

@Getter
    @Setter
    @Entity
    @NoArgsConstructor
    @AllArgsConstructor
    public class Ecopal {
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Long id;
        private String firstName;
        private String lastName;
        @NaturalId(mutable = true)
        private String email;
        private String password;
        private String username;
        private String phoneNumber;
        @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
        private Address address;
        @ElementCollection(fetch = FetchType.EAGER)
        private Set<Role> roles;
        private boolean isEnabled = false;
    }


