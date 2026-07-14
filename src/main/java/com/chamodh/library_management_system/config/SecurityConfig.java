package com.chamodh.library_management_system.config;

import com.chamodh.library_management_system.security.JwtAuthenticationFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;
    private final UserDetailsService userDetailsService;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
        // Spring builds this automatically once it knows about our
        // UserDetailsService and PasswordEncoder beans
    }

    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider(userDetailsService);
        // Constructor now takes UserDetailsService directly - this is the new pattern
        provider.setPasswordEncoder(passwordEncoder());
        // PasswordEncoder is set separately via setter, not the constructor
        return provider;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                // CSRF protection is for browser-based session/cookie auth -
                // irrelevant for a stateless JWT API, safe to disable here

                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                // STATELESS - tells Spring Security "don't create or use HTTP sessions,
                // every request must prove who it is via the JWT alone"

                .authorizeHttpRequests(auth -> auth
                                .requestMatchers("/api/auth/**").permitAll()

                                .requestMatchers(org.springframework.http.HttpMethod.GET,
                                        "/api/books/**", "/api/authors/**", "/api/categories/**").permitAll()

                                .requestMatchers("/api/books/**", "/api/authors/**", "/api/categories/**")
                                .hasRole("LIBRARIAN")

                                .requestMatchers("/api/members/**").hasRole("LIBRARIAN")

                                .requestMatchers("/api/borrow-records/**").authenticated()

                                .anyRequest().authenticated()
                        // Catch-all safety net - anything not explicitly listed above
                        // still requires authentication by default
                )

                .authenticationProvider(authenticationProvider())

                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);
        // Plugs our custom JWT filter into Spring Security's chain,
        // running BEFORE Spring's own username/password filter -
        // this is what actually makes our JwtAuthenticationFilter run on every request

        return http.build();
    }
}