package com.example.medical.store.Admin;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AdminService {
    @Autowired
    private static AdminRepo adminRepo;


    public static AdminModel adminCredentials(@Valid AdminModel adminModel) {
        Optional<AdminModel> existingAdmin = adminRepo.findByEmail(adminModel.getEmail());
        if (existingAdmin.isPresent()) {
            throw new IllegalArgumentException("Admin with this email already exists");
        }
        return adminRepo.save(adminModel);
    }

    public String adminLogin(@Valid String email, String password) {
        Optional<AdminModel> admin = adminRepo.findByEmail(email);
        if (admin.isEmpty() || !admin.get().getPassword().equals(password)) {
            throw new IllegalArgumentException("Invalid email or password");
        }
        return "Admin logged in successfully";
    }
}
