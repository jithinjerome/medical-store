package com.example.medical.store.JWT;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableMethodSecurity(prePostEnabled = true)
public class SecurityConfig {

    private static final Logger logger = LoggerFactory.getLogger(SecurityConfig.class);
    private final JWTAuthenticationFilter jwtAuthenticationFilter;

    public SecurityConfig(JWTAuthenticationFilter jwtAuthenticationFilter) {
        this.jwtAuthenticationFilter = jwtAuthenticationFilter;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .cors(cors -> cors.configure(http))
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth
                        // Publicly accessible endpoints
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
                                "/api/auth/medical-store/register",
                                "/api/auth/medical-store/login",
                                "/api/auth/medical-store/allEmployees",
                                "/api/bill/generate",
                                "/api/request/allRequests/{userId}",
                                "/api/payment/key",
                                "/api/payment/verify"
                        ).permitAll()

                        // Admin-only endpoints
                        .requestMatchers(
                                "/api/auth/admin/users",
                                "/api/auth/admin/medical-stores",
                                "/api/auth/admin/delivery-persons",
                                "/api/auth/admin/verifyMedicalStore/{id}",
                                "/api/auth/admin/revokeMedicalStore/{id}",
                                "/api/auth/admin/verifyDeliveryPerson/{id}",
                                "/api/auth/admin/revokeDeliveryPerson/{id}",
                                "/api/auth/admin/removeStore/{id}",
                                "/api/auth/admin/removeDeliveryPerson/{id}",
                                "/api/auth/admin/sendVerificationEmail",
                                "/api/auth/admin/allStores",
                                "/api/auth/admin/uploadLicense/{storeId}"
                        ).hasRole("ADMIN")

                        // Medical Store-specific endpoints
                        .requestMatchers(
                                "/api/auth/medical-store/allPrescriptions",
                                "/api/auth/medical-store/allEmployees",
                                "/api/auth/medical-store/allPrescriptions/{storeId}"
                             //   "/api/bill/generate"
                        ).hasRole("MEDICALSTORE")

                        // User-specific endpoints
                        .requestMatchers(
                                "/api/user/{id}",
                                "/api/user/{id}/updateDetails",
                                "/api/request/send",
                                "/api/bill/{userId}",
                                "/api/user/user-location",
                                "/api/prescription/upload/{userId}",
                                "/api/prescription/{id}"
                        ).hasRole("USER")

                        // Delivery Person-specific endpoints
                        .requestMatchers(
                                "/api/auth/delivery-people/allDeliveryPersons",
                                "/api/auth/delivery-people/verifiedPersons",
                                "/api/auth/delivery-people/notVerified"
                        ).hasRole("DELIVERYPERSON")

                        // Common endpoints for both Users and Admins
                        .requestMatchers("/api/auth/medical-store/verifiedStores")
                        .hasAnyRole("ADMIN", "USER")
                        .requestMatchers("/error").permitAll()

                        // Any other request requires authentication
                        .anyRequest().authenticated()
                )
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        logger.info("Security Configuration Initialized Successfully.");
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
