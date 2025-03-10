package com.example.medical.store.MedicalStore;

import com.example.medical.store.Admin.AdminModel;
import com.example.medical.store.JWT.JWTUtil;
import com.example.medical.store.Prescription.PrescriptionRequest;
import com.example.medical.store.Prescription.PrescriptionRequestRepository;
import com.example.medical.store.User.Role;
import com.example.medical.store.User.VerificationStatus;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class MedicalStoreService {

    @Autowired
    private MedicalStoreRepo medicalStoreRepo;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JWTUtil jwtUtil;

    @Autowired
    private PrescriptionRequestRepository prescriptionRequestRepository;

    public MedicalStoreModel registerMedicalStore(@Valid MedicalStoreModel medicalStoreModel) {
        Optional<MedicalStoreModel> existingMedicalStore = medicalStoreRepo.findByEmail(medicalStoreModel.getEmail());
        if (existingMedicalStore.isPresent()) {
            throw new IllegalArgumentException("Medical store with this email already exists");
        }
        if (medicalStoreModel.getVerificationStatus() == null) {
            medicalStoreModel.setVerificationStatus(VerificationStatus.NOT_VERIFIED);
        }
        if (medicalStoreModel.getRole() == null) {
            medicalStoreModel.setRole(Role.MEDICALSTORE);
        }
        medicalStoreModel.setPassword(passwordEncoder.encode(medicalStoreModel.getPassword()));

        return medicalStoreRepo.save(medicalStoreModel);
    }

    public String medicalStoreLogin( String email, String password) {
        Optional<MedicalStoreModel> medicalStoreOptional = medicalStoreRepo.findByEmail(email);
        if(medicalStoreOptional.isPresent()){
            MedicalStoreModel medicalStore = medicalStoreOptional.get();
            log.info("Medical store Login : "+medicalStore.getRole());
            if(passwordEncoder.matches(password, medicalStore.getPassword())){
                return jwtUtil.generateToken(medicalStore.getStoreId(),medicalStore.getEmail(), medicalStore.getRole().name());
            }
            throw new IllegalArgumentException("Invalid Credentials: Password Missmatch");
        }
        throw new IllegalArgumentException("Invalid Credentials: User not found");
    }

    public ResponseEntity<List<MedicalStoreModel>> allStores() {
        List<MedicalStoreModel> stores = medicalStoreRepo.findAll();
        return new ResponseEntity<>(stores, HttpStatus.OK);
    }

    public ResponseEntity<List<MedicalStoreModel>> verifiedStores() {
        List<MedicalStoreModel> verifiedStores = medicalStoreRepo.findByVerificationStatus(VerificationStatus.VERIFIED);

        if(verifiedStores.isEmpty()){
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(verifiedStores, HttpStatus.OK);
    }

    public ResponseEntity<List<MedicalStoreModel>> notVerifiedStores() {
        List<MedicalStoreModel> notVerifiedStores = medicalStoreRepo.findByVerificationStatus(VerificationStatus.NOT_VERIFIED);

        if(notVerifiedStores.isEmpty()){
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(notVerifiedStores, HttpStatus.OK);
    }


    public ResponseEntity<List<PrescriptionRequest>> allPrescriptions(int storeId) {
        Optional<MedicalStoreModel> medicalStoreModelOptional = medicalStoreRepo.findById(storeId);
        if(medicalStoreModelOptional.isPresent()){
            List<PrescriptionRequest> prescriptionRequestList = prescriptionRequestRepository.findByStoreId(storeId);
            return new ResponseEntity<>(prescriptionRequestList,HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);

    }
}
