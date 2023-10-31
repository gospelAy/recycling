package com.example.recyclingplastic.SecurityConfig.security.event.listener;

import com.example.recyclingplastic.models.Ecopal;
import lombok.Getter;
import lombok.Setter;
import org.springframework.context.ApplicationEvent;

@Getter
@Setter
public class RegistrationCompleteEvent extends ApplicationEvent {
    private Ecopal user;
    private String applicationUrl;

    public RegistrationCompleteEvent(Ecopal user, String applicationUrl) {
        super(user);
        this.user = user;
        this.applicationUrl = applicationUrl;
    }
}