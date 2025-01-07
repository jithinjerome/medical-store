package com.example.medical.store.MedicalStore;

import com.example.medical.store.Admin.AdminModel;
import com.example.medical.store.JWT.JWTUtil;
import com.example.medical.store.User.Role;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class MedicalStoreService {

    @Autowired
    private MedicalStoreRepo medicalStoreRepo;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JWTUtil jwtUtil;

    public MedicalStoreModel registerMedicalStore(@Valid MedicalStoreModel medicalStoreModel) {
        Optional<MedicalStoreModel> existingMedicalStore = medicalStoreRepo.findByEmail(medicalStoreModel.getEmail());
        if (existingMedicalStore.isPresent()) {
            throw new IllegalArgumentException("Medical store with this email already exists");
        }
        if (MedicalStoreModel.getRole() == null) {
            medicalStoreModel.setRole(Role.MEDICALSTORE);
        }
        medicalStoreModel.setPassword(passwordEncoder.encode(medicalStoreModel.getPassword()));

        return medicalStoreRepo.save(medicalStoreModel);
    }

    public String medicalStoreLogin( String email, String password) {
        Optional<MedicalStoreModel> medicalStoreOptional = medicalStoreRepo.findByEmail(email);
        if(medicalStoreOptional.isPresent()){
            MedicalStoreModel medicalStore = medicalStoreOptional.get();
            if(passwordEncoder.matches(password, medicalStore.getPassword())){
                return jwtUtil.generateToken(medicalStore.getEmail(), MedicalStoreModel.getRole().name());
            }
            throw new IllegalArgumentException("Invalid Credentials: Password Missmatch");
        }
        throw new IllegalArgumentException("Invalid Credentials: User not found");
    }
}
