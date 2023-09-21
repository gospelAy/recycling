package com.example.recyclingplastic.services;

import com.example.recyclingplastic.dto.CustomerResponse;
import com.example.recyclingplastic.dto.RegistrationRequest;
import com.example.recyclingplastic.models.Customer;
import com.example.recyclingplastic.SecurityConfig.security.token.VerificationToken;

import java.util.List;
import java.util.Optional;
public interface CustomerService {
    List<Customer> getUsers();
    Customer registerUser(RegistrationRequest request);
    Optional<Customer> findByEmail(String email);
    void saveUserVerificationToken(Customer theUser, String verificationToken);

    String validateToken(String theToken);

    VerificationToken generateNewVerificationToken(String oldToken);

    CustomerResponse getAllCustomer(int pageNo, int pageSize);
    RegistrationRequest getCustomerById(long id);
    RegistrationRequest updateCustomer(RegistrationRequest customerDto, long id);
    void deleteCustomerId(long id);
    String loginEngineer(String username, String password);
}