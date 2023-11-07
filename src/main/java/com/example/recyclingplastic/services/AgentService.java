package com.example.recyclingplastic.services;

import com.example.recyclingplastic.dto.request.AgentRegistrationDto;
import com.example.recyclingplastic.dto.response.AgentResponse;

public interface AgentService {
    AgentRegistrationDto createAgent(AgentRegistrationDto agentRegistrationDto);
    AgentResponse getAllAgent(int pageNo, int pageSize);
}
