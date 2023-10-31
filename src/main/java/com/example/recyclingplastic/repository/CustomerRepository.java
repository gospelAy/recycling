package com.example.recyclingplastic.repository;

import com.example.recyclingplastic.models.Ecopal;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CustomerRepository extends JpaRepository<Ecopal, Long> {
    Optional<Ecopal> findByEmail(String email);
}
