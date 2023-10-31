package com.example.recyclingplastic.dto.response;

import com.example.recyclingplastic.dto.request.RegistrationRequest;
import lombok.Data;

import java.util.List;

@Data
public class EcopalResponse {
    private List<RegistrationRequest> content;
    private int pageNo;
    private int pageSize;
    private long totalElements;
    private int totalPages;
    private boolean last;
}
