package com.example.recyclingplastic.dto.response;

import com.example.recyclingplastic.dto.request.AdminRegistrationDto;
import lombok.Data;

import java.util.List;

@Data
public class AdminResponse {
    private List<AdminRegistrationDto> content;
    private int pageNo;
    private int pageSize;
    private long totalElement;
    private int totalPage;
    private boolean last;
}