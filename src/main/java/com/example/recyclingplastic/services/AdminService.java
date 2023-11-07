package com.example.recyclingplastic.services;

import com.example.recyclingplastic.dto.request.AdminRegistrationDto;
import com.example.recyclingplastic.dto.response.AdminResponse;
;

public interface AdminService {
    AdminRegistrationDto createAdmin(AdminRegistrationDto adminRegistrationDto);
    AdminResponse getAllAdmin(int pageNo, int pageSize);
    AdminRegistrationDto getAdminById(Long id);
    AdminRegistrationDto updateAdmin(AdminRegistrationDto adminRegistrationDto, Long id);
    void deleteAdmin(Long adminId);
}
