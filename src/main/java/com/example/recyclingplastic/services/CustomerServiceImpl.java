package com.example.recyclingplastic.services;

import com.example.recyclingplastic.dto.CustomerResponse;
import com.example.recyclingplastic.dto.RegistrationRequest;
import com.example.recyclingplastic.exceptions.CustomerNotFoundException;
import com.example.recyclingplastic.repository.CustomerRepository;
import com.example.recyclingplastic.models.Customer;
import com.example.recyclingplastic.SecurityConfig.security.token.VerificationToken;
import com.example.recyclingplastic.SecurityConfig.security.token.VerificationTokenRepository;
import com.example.recyclingplastic.exceptions.UserAlreadyExistsException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Calendar;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CustomerServiceImpl implements CustomerService {
    private final CustomerRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final VerificationTokenRepository tokenRepository;

    @Override
    public List<Customer> getUsers() {
        return userRepository.findAll();
    }

    @Override
    public Customer registerUser(RegistrationRequest request) {
        Optional<Customer> user = this.findByEmail(request.email());
        if (user.isPresent()) {
            throw new UserAlreadyExistsException(
                    "User with email " + request.email() + " already exists");
        }
        var newUser = new Customer();
        newUser.setFirstName(request.firstName());
        newUser.setLastName(request.lastName());
        newUser.setEmail(request.email());
        newUser.setPassword(passwordEncoder.encode(request.password()));
        newUser.setRole(request.role());
        return userRepository.save(newUser);
    }

    @Override
    public Optional<Customer> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    @Override
    public void saveUserVerificationToken(Customer theUser, String token) {
        var verificationToken = new VerificationToken(token, theUser);
        tokenRepository.save(verificationToken);
    }

    @Override
    public String validateToken(String theToken) {
        VerificationToken token = tokenRepository.findByToken(theToken);
        if (token == null) {
            return "Invalid verification token";
        }
        Customer user = token.getUser();
        Calendar calendar = Calendar.getInstance();
        if ((token.getExpirationTime().getTime() - calendar.getTime().getTime()) <= 0) {
            return "Verification link already expired," +
                    " Please, click the link below to receive a new verification link";
        }
        user.setEnabled(true);
        userRepository.save(user);
        return "valid";
    }

    @Override
    public VerificationToken generateNewVerificationToken(String oldToken) {
        VerificationToken verificationToken = tokenRepository.findByToken(oldToken);
        var tokenExpirationTime = new VerificationToken();
        verificationToken.setToken(UUID.randomUUID().toString());
        verificationToken.setExpirationTime(tokenExpirationTime.getTokenExpirationTime());
        return tokenRepository.save(verificationToken);
    }

    @Override
    public CustomerResponse getAllCustomer(int pageNo, int pageSize) {
        Pageable pageable = PageRequest.of(pageNo, pageSize);
        Page<Customer> customers = userRepository.findAll(pageable);
        List<Customer> listOfCustomer = customers.getContent();
        List<RegistrationRequest> content = listOfCustomer.stream().map(p -> mapToDto(p)).collect(Collectors.toList());

        CustomerResponse customerResponse = new CustomerResponse();
        customerResponse.setContent(content);
        customerResponse.setPageNo(customers.getNumber());
        customerResponse.setPageSize(customers.getSize());
        customerResponse.setTotalElements(customers.getTotalElements());
        customerResponse.setTotalPages(customers.getTotalPages());
        customerResponse.setLast(customers.isLast());
        return customerResponse;
    }

    @Override
    public RegistrationRequest getCustomerById(long id) {
        Customer customer = userRepository.findById(id).orElseThrow(() -> new CustomerNotFoundException("customer could not be found"));
        return mapToDto(customer);
    }


    @Override
    public RegistrationRequest updateCustomer(RegistrationRequest customerDto, long id) {
        Customer customer = userRepository.findById(id).orElseThrow(() ->
                new CustomerNotFoundException("customer could not be updated"));

        customer.setFirstName(customerDto.firstName());
        customer.setLastName(customerDto.lastName());
        customer.setEmail(customerDto.email());
        customer.setPassword(customerDto.password());

        Customer updateCustomer = userRepository.save(customer);
        return mapToDto(updateCustomer);
    }

    @Override
    public void deleteCustomerId(long id) {
        Customer customer = userRepository.findById(id).orElseThrow(() -> new CustomerNotFoundException("Customer could not be deleted"));
        userRepository.delete(customer);
    }

    @Override
    public String loginEngineer(String email, String password) {
        Optional<Customer> optionalCustomer = userRepository.findByEmail(email);
        if (optionalCustomer.isEmpty()) {
            return "You are not registered";
        }
        Customer customer = optionalCustomer.get();
        if (!customer.getPassword().equals(password)) {
            return "Incorrect password";
        }
        return "Login successful";
    }


    private RegistrationRequest mapToDto(Customer customer){
        return new RegistrationRequest(
                customer.getFirstName(),
                customer.getLastName(),
                customer.getEmail(),
                customer.getPassword(),
                customer.getRole()
        );
    }
}
