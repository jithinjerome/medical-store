package com.example.medical.store.MedicalStore;

import com.example.medical.store.Admin.AdminModel;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class MedicalStoreService {

    @Autowired
    private MedicalStoreRepo medicalStoreRepo;

    public MedicalStoreModel registerMedicalStore(@Valid MedicalStoreModel medicalStoreModel) {
        Optional<MedicalStoreModel> existingMedicalStore = medicalStoreRepo.findByEmail(medicalStoreModel.getEmail());
        if (existingMedicalStore.isPresent()) {
            throw new IllegalArgumentException("Medical store with this email already exists");
        }
        return medicalStoreRepo.save(medicalStoreModel);
    }

    public String medicalStoreLogin( String email, String password) {
        Optional<MedicalStoreModel> medicalStore = medicalStoreRepo.findByEmail(email);
        if (medicalStore.isEmpty() || !medicalStore.get().getPassword().equals(password)) {
            throw new IllegalArgumentException("Invalid email or password");
        }
        return "Medical store logged in successfully";
    }
}
