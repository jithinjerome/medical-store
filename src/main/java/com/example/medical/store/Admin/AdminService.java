package com.example.medical.store.Admin;


import com.example.medical.store.DeliveryPerson.DeliveryPersonModel;
import com.example.medical.store.DeliveryPerson.DeliveryPersonRepo;
import com.example.medical.store.JWT.JWTUtil;
import com.example.medical.store.MedicalStore.MedicalStoreModel;
import com.example.medical.store.MedicalStore.MedicalStoreRepo;
import com.example.medical.store.User.VerificationStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.kafka.KafkaProperties;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.swing.text.html.Option;
import java.util.Optional;

@Service
public class AdminService {
    @Autowired
    private AdminRepo adminRepo;

    @Autowired
    private DeliveryPersonRepo deliveryPersonRepo;

    @Autowired
    private MedicalStoreRepo medicalStoreRepo;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JWTUtil jwtUtil;


    public String adminLogin( String email, String password) {
        Optional<AdminModel> adminOptional = adminRepo.findByEmail(email);
        if(adminOptional.isPresent()){
            AdminModel admin = adminOptional.get();

            if(passwordEncoder.matches(password, admin.getPassword())){
                return jwtUtil.generateToken(admin.getAdminId(),admin.getEmail(), admin.getRole().name());
            }else{
                throw new IllegalArgumentException("Invalid Credentials: Password mismatch");
            }
        }
        throw new IllegalArgumentException("Invalid Credentials: User not found");

    }

    public DeliveryPersonModel verifiedPerson(int id){
        Optional<DeliveryPersonModel> deliveryPerson = deliveryPersonRepo.findById(id);
        if(deliveryPerson.isPresent()){
            DeliveryPersonModel persons = deliveryPerson.get();
            persons.setVerificationStatus(VerificationStatus.VERIFIED);
            return deliveryPersonRepo.save(persons);
        }else {
            throw new IllegalArgumentException("No person found with this ID");
        }
    }

    public MedicalStoreModel verifiedStore(int id) {
        Optional<MedicalStoreModel> medicalStore = medicalStoreRepo.findById(id);
        if(medicalStore.isPresent()){
            MedicalStoreModel verifiedStore = medicalStore.get();
            verifiedStore.setVerificationStatus(VerificationStatus.VERIFIED);
            return medicalStoreRepo.save(verifiedStore);
        }else{
            throw new IllegalArgumentException("No store found with this ID");
        }
    }
}
