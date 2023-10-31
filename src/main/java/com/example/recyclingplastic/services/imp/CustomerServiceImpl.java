package com.example.recyclingplastic.services.imp;

import com.example.recyclingplastic.dto.request.EcopalProfileDto;
import com.example.recyclingplastic.dto.response.EcopalResponse;
import com.example.recyclingplastic.dto.request.RegistrationRequest;
import com.example.recyclingplastic.exceptions.CustomerNotFoundException;
import com.example.recyclingplastic.exceptions.UserProfileNotFoundException;
import com.example.recyclingplastic.repository.CustomerRepository;
import com.example.recyclingplastic.models.Ecopal;
import com.example.recyclingplastic.SecurityConfig.security.token.VerificationToken;
import com.example.recyclingplastic.SecurityConfig.security.token.VerificationTokenRepository;
import com.example.recyclingplastic.exceptions.UserAlreadyExistsException;
import com.example.recyclingplastic.services.CustomerService;
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
    public List<Ecopal> getUsers() {
        return userRepository.findAll();
    }

    @Override
    public Ecopal registerUser(RegistrationRequest request) {
        Optional<Ecopal> user = this.findByEmail(request.email());
        if (user.isPresent()) {
            throw new UserAlreadyExistsException(
                    "User with email " + request.email() + " already exists");
        }
        var newUser = new Ecopal();
        newUser.setFirstName(request.firstName());
        newUser.setLastName(request.lastName());
        newUser.setEmail(request.email());
        newUser.setPassword(passwordEncoder.encode(request.password()));
        newUser.setRole(request.role());
        return userRepository.save(newUser);
    }

    @Override
    public Optional<Ecopal> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    @Override
    public void saveUserVerificationToken(Ecopal theUser, String token) {
        var verificationToken = new VerificationToken(token, theUser);
        tokenRepository.save(verificationToken);
    }

    @Override
    public String validateToken(String theToken) {
        VerificationToken token = tokenRepository.findByToken(theToken);
        if (token == null) {
            return "Invalid verification token";
        }
        Ecopal user = token.getUser();
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
    public EcopalResponse getAllCustomer(int pageNo, int pageSize) {
        Pageable pageable = PageRequest.of(pageNo, pageSize);
        Page<Ecopal> customers = userRepository.findAll(pageable);
        List<Ecopal> listOfEcopal = customers.getContent();
        List<RegistrationRequest> content = listOfEcopal.stream().map(p -> mapToDto(p)).collect(Collectors.toList());

        EcopalResponse ecopalResponse = new EcopalResponse();
        ecopalResponse.setContent(content);
        ecopalResponse.setPageNo(customers.getNumber());
        ecopalResponse.setPageSize(customers.getSize());
        ecopalResponse.setTotalElements(customers.getTotalElements());
        ecopalResponse.setTotalPages(customers.getTotalPages());
        ecopalResponse.setLast(customers.isLast());
        return ecopalResponse;
    }

    @Override
    public RegistrationRequest getCustomerById(long id) {
        Ecopal ecopal = userRepository.findById(id).orElseThrow(() -> new CustomerNotFoundException("customer could not be found"));
        return mapToDto(ecopal);
    }

    @Override
    public RegistrationRequest updateCustomer(RegistrationRequest customerDto, long id) {
        Ecopal ecopal = userRepository.findById(id).orElseThrow(() ->
                new CustomerNotFoundException("customer could not be updated"));

        ecopal.setFirstName(customerDto.firstName());
        ecopal.setLastName(customerDto.lastName());
        ecopal.setEmail(customerDto.email());
        ecopal.setPassword(customerDto.password());

        Ecopal updateEcopal = userRepository.save(ecopal);
        return mapToDto(updateEcopal);
    }

    @Override
    public void deleteCustomerId(long id) {
        Ecopal ecopal = userRepository.findById(id).orElseThrow(() -> new CustomerNotFoundException("Customer could not be deleted"));
        userRepository.delete(ecopal);
    }

    @Override
    public String loginEngineer(String email, String password) {
        Optional<Ecopal> optionalCustomer = userRepository.findByEmail(email);
        if (optionalCustomer.isEmpty()) {
            return "You are not registered";
        }
        Ecopal ecopal = optionalCustomer.get();
        if (!ecopal.getPassword().equals(password)) {
            return "Incorrect password";
        } else if (!ecopal.getEmail().equals(email)) {
            return "Incorrect password";
        }
        return "Login successful";
    }

    @Override
    public EcopalProfileDto getUserProfileDtoById(Long userId) {
        Optional<Ecopal> userProfile = userRepository.findById(userId);
        if (userProfile.isPresent()){
            Ecopal user = userProfile.get();
            EcopalProfileDto ecopalProfileDto = new EcopalProfileDto();
            ecopalProfileDto.setFirstname(user.getFirstName());
            ecopalProfileDto.setPhoneNumber(user.getPhoneNumber());
            return ecopalProfileDto;
        }else {
            throw new UserProfileNotFoundException("user profile not found for userId: " + userId);
        }
    }


    private RegistrationRequest mapToDto(Ecopal ecopal){
        return new RegistrationRequest(
                ecopal.getFirstName(),
                ecopal.getLastName(),
                ecopal.getEmail(),
                ecopal.getPassword(),
                ecopal.getRole()
        );
    }
}
