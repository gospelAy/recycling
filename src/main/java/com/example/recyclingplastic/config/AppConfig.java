package com.example.recyclingplastic.config;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AppConfig {
    @Value("${cloudinary_name}")
    private String cloudinaryName;
    @Value("${cloudinary_key}")
    private String cloudinaryApiKey;
    @Value("${cloudinary_secret}")
    private String cloudinarySecret;


    @Bean
    public Cloudinary  cloudinary(){
        return new Cloudinary(
                ObjectUtils.asMap(
                        "cloud_name", cloudinaryName,
                        "api_key", cloudinaryApiKey,
                        "api_secret", cloudinarySecret
                ));
    }
}
