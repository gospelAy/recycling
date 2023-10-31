package com.example.recyclingplastic.repository;

import com.example.recyclingplastic.models.Agent;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AgentRepository extends JpaRepository<Agent, Long> {
}
