package com.chamodh.library_management_system.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
// @Configuration tells Spring "this class defines beans, scan it at startup"
public class SecurityConfig {

    @Bean
    // @Bean tells Spring "call this method once, and manage the returned
    // object as a singleton bean available for injection elsewhere"
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}