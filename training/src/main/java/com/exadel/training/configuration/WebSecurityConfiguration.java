package com.exadel.training.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@ComponentScan("com.exadel.training.security")
public class WebSecurityConfiguration {
    private PasswordEncoder encoder;

    @Bean
    public PasswordEncoder passwordEncoder() {
        if (encoder == null) {
            encoder = new BCryptPasswordEncoder();
        }
        return encoder;
    }
}
