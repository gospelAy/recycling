package com.example.recyclingplastic.services.imp;
import com.example.recyclingplastic.dto.request.AdminRegistrationDto;
import com.example.recyclingplastic.dto.response.AdminResponse;
import com.example.recyclingplastic.exceptions.AdminNotFoundException;
import com.example.recyclingplastic.models.Admin;
import com.example.recyclingplastic.repository.AdminRepository;
import com.example.recyclingplastic.services.AdminService;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;

@AllArgsConstructor
@Service
public class AdminServiceImpl implements AdminService {
    private AdminRepository adminRepository;

    @Override
    public AdminRegistrationDto createAdmin(AdminRegistrationDto adminRegistrationDto) {
        Admin admin = new Admin();
        admin.setPassword(adminRegistrationDto.getPassword());
        admin.setLastname(adminRegistrationDto.getLastname());
        admin.setEmail(adminRegistrationDto.getEmail());
        admin.setPassword(adminRegistrationDto.getPassword());

        Admin newAdmin = adminRepository.save(admin);

        AdminRegistrationDto adminResponse = new AdminRegistrationDto();
        adminResponse.setFirstname(newAdmin.getFirstname());
        adminResponse.setLastname(newAdmin.getLastname());
        adminResponse.setEmail(newAdmin.getEmail());
        adminResponse.setPassword(newAdmin.getPassword());
        return adminResponse;

    }

    @Override
    public AdminResponse getAllAdmin(int pageNo, int pageSize) {
        Pageable pageable = PageRequest.of(pageNo, pageSize);
        Page<Admin> admin = adminRepository.findAll(pageable);
        List<Admin> listOfAdmin = admin.getContent();
        List<AdminRegistrationDto> content =listOfAdmin.stream().map(p->mapToDto(p)).collect(Collectors.toList());

        AdminResponse adminResponse = new AdminResponse();
        adminResponse.setContent(content);
        adminResponse.setPageNo(admin.getNumber());
        adminResponse.setPageSize(admin.getSize());
        adminResponse.setTotalElement(admin.getTotalElements());
        adminResponse.setTotalPage(admin.getTotalPages());
        adminResponse.setLast(admin.isLast());
        return adminResponse;
    }

    @Override
    public AdminRegistrationDto getAdminById(Long id) {
        Admin admin = adminRepository.findById(id).orElseThrow(()-> new AdminNotFoundException("Admin could not be found"));
        return mapToDto(admin);
    }

    @Override
    public AdminRegistrationDto updateAdmin(AdminRegistrationDto adminRegistrationDto, Long id) {
        Admin admin = adminRepository.findById(id).orElseThrow(()-> new AdminNotFoundException("Admin Could not be found"));
        admin.setFirstname(adminRegistrationDto.getFirstname());
        admin.setLastname(adminRegistrationDto.getLastname());
        admin.setEmail(adminRegistrationDto.getEmail());
        Admin updateAdmin = adminRepository.save(admin);
        return mapToDto(updateAdmin);
    }

    @Override
    public void deleteAdmin(Long adminId) {
        Admin admin = adminRepository.findById(adminId).orElseThrow(()-> new AdminNotFoundException("Admin Could not be found "));
        if (!adminId.equals(admin.getId())){
            throw new AdminNotFoundException("it is no correct please check and try again");
        }
        adminRepository.delete(admin);
    }

    private AdminRegistrationDto mapToDto(Admin admin) {
        AdminRegistrationDto adminRegistrationDto = new AdminRegistrationDto();
        adminRegistrationDto.setId(admin.getId());
        adminRegistrationDto.setFirstname(admin.getFirstname());
        adminRegistrationDto.setLastname(admin.getLastname());
        adminRegistrationDto.setEmail(admin.getEmail());
        adminRegistrationDto.setPassword(admin.getPassword());
        return adminRegistrationDto;
    }
}
