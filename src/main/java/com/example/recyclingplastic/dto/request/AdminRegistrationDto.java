package com.example.recyclingplastic.dto.request;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class AdminRegistrationDto {
    private long id;
    private String firstname;
    private String lastname;
    private String email;
    private String password;

}
