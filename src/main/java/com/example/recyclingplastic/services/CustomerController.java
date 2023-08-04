//package com.example.recyclingplastic.services;
//
//import com.example.recyclingplastic.models.Customer;
//import com.example.recyclingplastic.services.CustomerServiceImpl;
//import lombok.RequiredArgsConstructor;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RestController;
//
//import java.util.List;
//
//@RestController
//@RequiredArgsConstructor
//@RequestMapping("/users")
//public class CustomerController {
//    private final CustomerServiceImpl userService;
//
//    @GetMapping
//    public List<Customer> getUsers(){
//        return userService.getUsers();
//    }
//}