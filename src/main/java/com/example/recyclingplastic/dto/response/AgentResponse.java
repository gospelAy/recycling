package com.example.recyclingplastic.dto.response;

import com.example.recyclingplastic.dto.request.AgentRegistrationDto;
import lombok.Data;

import java.util.List;
@Data
public class AgentResponse {
    private List<AgentRegistrationDto> content;
    private int pageNo;
    private int pageSize;
    private long totalElement;
    private int totalPages;
    private boolean last;
}
