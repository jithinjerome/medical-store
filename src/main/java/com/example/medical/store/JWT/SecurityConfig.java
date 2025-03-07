package com.example.medical.store.JWT;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableMethodSecurity
public class SecurityConfig {
    private final JWTAuthenticationFilter jwtAuthenticationFilter;

    public SecurityConfig(JWTAuthenticationFilter jwtAuthenticationFilter){
        this.jwtAuthenticationFilter = jwtAuthenticationFilter;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception{
        http
                .csrf(AbstractHttpConfigurer::disable)
                .cors(Customizer.withDefaults())
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(
                                "/api/auth/refreshToken",
                                "/api/user/register",
                                "/api/user/login",
                                "/api/user/forgot-password",
                                "/api/user/verify",
                                "/api/user/reset-password",
                                "/api/auth/delivery-person/register",
                                "/api/auth/delivery-person/login",
                                "/api/auth/admin/login",
                                "/api/auth/admin/users",
                                "/api/auth/admin/medical-stores",
                                "/api/auth/admin/delivery-persons",
                                "/api/auth/medical-store/register",
                                "/api/auth/medical-store/login",
                                "/api/auth/admin/verifyMedicalStore/{id}",
                                "/api/auth/admin/revokeMedicalStore/{id}",
                                "/api/auth/admin/verifyDeliveryPerson/{id}",
                                "/api/auth/admin/revokeDeliveryPerson/{id}",
                                "/api/auth/admin/removeStore/{id}",
                                "/api/auth/admin/removeDeliveryPerson/{id}",
                                "/api/auth/admin/sendVerificationEmail"
                        ).permitAll()
                        .requestMatchers(
                                "/api/auth/admin/verify/{id}",
                                "/api/auth/admin/login",
                                "/api/auth/admin/users",
                                "/api/auth/admin/delivery-persons",
                                "/api/auth/admin/medical-stores",
                                "/api/auth/admin/verifyMedicalStore/{id}",
                                "/api/auth/admin/revokeMedicalStore/{id}",
                                "/api/auth/admin/verifyDeliveryPerson/{id}",
                                "/api/auth/admin/revokeDeliveryPerson/{id}",
                                "/api/auth/admin/removeStore/{id}",
                                "/api/auth/admin/removeDeliveryPerson/{id}",
                                "/api/auth/admin/sendVerificationEmail"
                        ).hasRole("ADMIN")
                        .requestMatchers
                                (
                                        "/api/user/{id}",
                                        "/api/user/{id}/updateDetails",
                                        "/api/request/send",
                                        "/api/bill/{userId}"
                                ).hasRole("USER")
                        .requestMatchers(
                                "/api/bill/generate",
                                "/api/auth/medicalstore/allPrescriptions/{storeId}",
                                "/api/auth/medical-store/register",
                                "/api/auth/medical-store/login"
                        ).hasRole("MEDICALSTORE")
                        .anyRequest().authenticated()
                )
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

}
