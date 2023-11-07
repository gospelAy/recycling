package com.example.recyclingplastic.controller;

import com.example.recyclingplastic.SecurityConfig.security.event.listener.RegistrationCompleteEvent;
import com.example.recyclingplastic.SecurityConfig.security.event.listener.RegistrationCompleteEventListener;
import com.example.recyclingplastic.SecurityConfig.security.token.VerificationToken;
import com.example.recyclingplastic.SecurityConfig.security.token.VerificationTokenRepository;
import com.example.recyclingplastic.dto.request.EcopalProfileDto;
import com.example.recyclingplastic.dto.request.LoginRequest;
import com.example.recyclingplastic.dto.request.RegistrationRequest;
import com.example.recyclingplastic.dto.response.EcopalResponse;
import com.example.recyclingplastic.exceptions.CustomerNotFoundException;
import com.example.recyclingplastic.exceptions.UserProfileNotFoundException;
import com.example.recyclingplastic.models.Ecopal;
import com.example.recyclingplastic.services.imp.CloudinaryServiceImpl;
import com.example.recyclingplastic.services.imp.CustomerServiceImpl;
import jakarta.mail.MessagingException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.UnsupportedEncodingException;

@Slf4j
@RestController
@CrossOrigin(origins = "*")
@RequiredArgsConstructor
@RequestMapping("/register")
public class CustomerRegistrationController {

    private final CustomerServiceImpl userService;
    private final ApplicationEventPublisher publisher;
    private final VerificationTokenRepository tokenRepository;
    private final RegistrationCompleteEventListener eventListener;
    private final CloudinaryServiceImpl cloudinaryService;

    @PostMapping
    public String registerUser(@RequestBody RegistrationRequest registrationRequest, final HttpServletRequest request){
        Ecopal user = userService.registerUser(registrationRequest);
        publisher.publishEvent(new RegistrationCompleteEvent(user, applicationUrl(request)));
        return "Success!  Please, check your email for to complete your registration";
    }

    @GetMapping("/verifyEmail")
    public String sendVerificationToken(@RequestParam("token") String token){
        VerificationToken theToken = tokenRepository.findByToken(token);
        if (theToken.getUser().isEnabled()){
            return "This account has already been verified, please, login.";
        }
        String verificationResult = userService.validateToken(token);
        if (verificationResult.equalsIgnoreCase("valid")){
            return "Email verified successfully. Now you can login to your account";
        }
        return "Invalid verification link, please, check your email for new verification link";
    }

    @GetMapping("/resend-verification-token")
    public String resendVerificationToken(@RequestParam("token") String oldToken, HttpServletRequest request)
            throws MessagingException, UnsupportedEncodingException {
        VerificationToken verificationToken =  userService.generateNewVerificationToken(oldToken);
        Ecopal theUser = verificationToken.getUser();
        resendVerificationTokenEmail(theUser, applicationUrl(request), verificationToken);
        return "A new verification link has been sent to your email," +
                " please, check to complete your registration";
    }

    private void resendVerificationTokenEmail(Ecopal theUser, String applicationUrl, VerificationToken token)
            throws MessagingException, UnsupportedEncodingException {
        String url = applicationUrl+"/register/verifyEmail?token="+token.getToken();
        eventListener.sendVerificationEmail(url);
        log.info("Click the link to verify your registration :  {}", url);
    }

    public String applicationUrl(HttpServletRequest request) {
        return "http://"+request.getServerName()+":"
                +request.getServerPort()+request.getContextPath();
    }

    @GetMapping("customers")
    public ResponseEntity<EcopalResponse> getCustomers(
            @RequestParam(value = "pageNo", defaultValue = "0", required = false) int pageNo,
            @RequestParam(value = "pageSize", defaultValue = "10", required = false) int pageSize
    ){
        return new ResponseEntity<>(userService.getAllCustomer(pageNo, pageSize), HttpStatus.OK);
    }

    @GetMapping( "customer/{id}")
    public ResponseEntity<RegistrationRequest> customerDetail(@PathVariable long id){
        return ResponseEntity.ok(userService.getCustomerById(id));
    }


    @PutMapping("customer/{id}/update")
    public ResponseEntity<RegistrationRequest> updateCustomer(@RequestBody RegistrationRequest customerDto, @PathVariable("id") long customerId) {
        RegistrationRequest response = userService.updateCustomer(customerDto, customerId);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @DeleteMapping("customer/{id}/delete")
    public ResponseEntity<String> deleteCustomer(@PathVariable("id") long customerId) {
        userService.deleteCustomerId(customerId);
        return new ResponseEntity<>("customer delete", HttpStatus.OK);
    }

    @PostMapping("customer/login")
    public ResponseEntity<String> loginEngineer(@RequestBody LoginRequest loginRequest) {
        String loginResponse = userService.loginEngineer(loginRequest.getEmail(), loginRequest.getPassword());
        return ResponseEntity.ok(loginResponse);
    }

    @GetMapping("/{userId}")
    public ResponseEntity<EcopalProfileDto> getUserProfile(@PathVariable Long userId) {
        try {
            EcopalProfileDto userProfile = userService.getUserProfileDtoById(userId);
            return ResponseEntity.ok(userProfile);
        } catch (UserProfileNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/image")
    public ResponseEntity<String> uploadImage(@RequestParam("image") MultipartFile image) {
        try {
            String imageUrl = cloudinaryService.upload(image);
            return ResponseEntity.ok(imageUrl);
        } catch (CustomerNotFoundException Ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to upload image");
        }
    }
}