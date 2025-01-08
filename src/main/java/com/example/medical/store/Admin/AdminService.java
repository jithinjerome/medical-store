package com.example.medical.store.Admin;


import com.example.medical.store.JWT.JWTUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.kafka.KafkaProperties;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AdminService {
    @Autowired
    private AdminRepo adminRepo;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JWTUtil jwtUtil;


    public String adminLogin( String email, String password) {
        Optional<AdminModel> adminOptional = adminRepo.findByEmail(email);
        if(adminOptional.isPresent()){
            AdminModel admin = adminOptional.get();

            if(passwordEncoder.matches(password, admin.getPassword())){
                return jwtUtil.generateToken(admin.getEmail(), admin.getRole().name());
            }else{
                throw new IllegalArgumentException("Invalid Credentials: Password mismatch");
            }
        }
        throw new IllegalArgumentException("Invalid Credentials: User not found");

    }
}
