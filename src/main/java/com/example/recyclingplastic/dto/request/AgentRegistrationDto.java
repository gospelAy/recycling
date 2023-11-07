package com.example.recyclingplastic.dto.request;

import lombok.Data;

@Data
public class AgentRegistrationDto {
    private long id;
    private String firstname;
    private String lastname;
    private String email;
    private String password;
}
