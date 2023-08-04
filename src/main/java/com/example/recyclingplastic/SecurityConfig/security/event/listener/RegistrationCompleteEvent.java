package com.example.recyclingplastic.SecurityConfig.security.event.listener;

import com.example.recyclingplastic.models.Customer;
import lombok.Getter;
import lombok.Setter;
import org.springframework.context.ApplicationEvent;

@Getter
@Setter
public class RegistrationCompleteEvent extends ApplicationEvent {
    private Customer user;
    private String applicationUrl;

    public RegistrationCompleteEvent(Customer user, String applicationUrl) {
        super(user);
        this.user = user;
        this.applicationUrl = applicationUrl;
    }
}