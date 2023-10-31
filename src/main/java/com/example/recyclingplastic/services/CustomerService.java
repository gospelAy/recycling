package com.example.recyclingplastic.services;

import com.example.recyclingplastic.dto.request.EcopalProfileDto;
import com.example.recyclingplastic.dto.response.EcopalResponse;
import com.example.recyclingplastic.dto.request.RegistrationRequest;
import com.example.recyclingplastic.models.Ecopal;
import com.example.recyclingplastic.SecurityConfig.security.token.VerificationToken;

import java.util.List;
import java.util.Optional;
public interface CustomerService {
    List<Ecopal> getUsers();
    Ecopal registerUser(RegistrationRequest request);
    Optional<Ecopal> findByEmail(String email);
    void saveUserVerificationToken(Ecopal theUser, String verificationToken);

    String validateToken(String theToken);

    VerificationToken generateNewVerificationToken(String oldToken);

    EcopalResponse getAllCustomer(int pageNo, int pageSize);
    RegistrationRequest getCustomerById(long id);
    RegistrationRequest updateCustomer(RegistrationRequest customerDto, long id);
    void deleteCustomerId(long id);
    String loginEngineer(String username, String password);
    EcopalProfileDto getUserProfileDtoById(Long userId);
}