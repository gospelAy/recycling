package com.example.recyclingplastic.repository;

import com.example.recyclingplastic.models.Admin;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AdminRepository extends JpaRepository<Admin, Long> {
}
