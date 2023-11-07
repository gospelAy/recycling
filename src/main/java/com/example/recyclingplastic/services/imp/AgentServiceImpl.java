package com.example.recyclingplastic.services.imp;

import com.example.recyclingplastic.dto.request.AgentRegistrationDto;
import com.example.recyclingplastic.dto.response.AgentResponse;
import com.example.recyclingplastic.models.Agent;
import com.example.recyclingplastic.repository.AgentRepository;
import com.example.recyclingplastic.services.AgentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class AgentServiceImpl implements AgentService {

    private AgentRepository agentRepository;

    @Autowired
    public AgentServiceImpl(AgentRepository agentRepository) {
        this.agentRepository = agentRepository;
    }

    @Override
    public AgentRegistrationDto createAgent(AgentRegistrationDto agentRegistrationDto) {
        Agent agent = new Agent();
        agent.setFirstname(agentRegistrationDto.getFirstname());
        agent.setLastname(agentRegistrationDto.getLastname());
        agent.setEmail(agentRegistrationDto.getEmail());
        agent.setPassword(agentRegistrationDto.getPassword());
        Agent newAgent = agentRepository.save(agent);

        AgentRegistrationDto response = new AgentRegistrationDto();
        response.setFirstname(newAgent.getFirstname());
        response.setLastname(newAgent.getLastname());
        response.setEmail(newAgent.getEmail());
        response.setPassword(newAgent.getPassword());
        return response;
    }

    @Override
    public AgentResponse getAllAgent(int pageNo, int pageSize) {
        Pageable pageable = PageRequest.of(pageNo, pageSize);
        Page<Agent> agents = agentRepository.findAll(pageable);
        List<Agent> listOfAgent = agents.getContent();
        List<AgentRegistrationDto> content = listOfAgent.stream().map(p-> mapToDto(p)).collect(Collectors.toList());
        return null;
    }

    private AgentRegistrationDto mapToDto(Agent agent) {
        AgentRegistrationDto agentRegistrationDto = new AgentRegistrationDto();
        agentRegistrationDto.setId(agent.getId());
        agentRegistrationDto.setFirstname(agent.getFirstname());
        agentRegistrationDto.setLastname(agent.getLastname());
        agentRegistrationDto.setEmail(agent.getEmail());
        agentRegistrationDto.setPassword(agent.getPassword());
        return agentRegistrationDto;
    }

}
