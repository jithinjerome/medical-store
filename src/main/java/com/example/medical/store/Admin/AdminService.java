package com.example.medical.store.Admin;


import com.example.medical.store.DeliveryPerson.DeliveryPersonModel;
import com.example.medical.store.DeliveryPerson.DeliveryPersonRepo;
import com.example.medical.store.JWT.JWTUtil;
import com.example.medical.store.MedicalStore.MedicalStoreModel;
import com.example.medical.store.MedicalStore.MedicalStoreRepo;
import com.example.medical.store.User.VerificationStatus;
import com.example.medical.store.User.User;
import com.example.medical.store.User.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.swing.text.html.Option;
import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Service
public class AdminService {
    @Autowired
    private AdminRepo adminRepo;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JWTUtil jwtUtil;


    @Autowired
    private MedicalStoreRepo medicalStoreRepo;

    @Autowired
    private DeliveryPersonRepo deliveryPersonRepo;

    @Autowired
    private UserRepository userRepo;



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
    public MedicalStoreModel revokeVerifiedStore(int id) {
        Optional<MedicalStoreModel> medicalStore = medicalStoreRepo.findById(id);
        if(medicalStore.isPresent()){
            MedicalStoreModel revokeVerifiedStore = medicalStore.get();
            revokeVerifiedStore.setVerificationStatus(VerificationStatus.NOT_VERIFIED);
            return medicalStoreRepo.save(revokeVerifiedStore);
        }else{
            throw new IllegalArgumentException("No store found with this ID");
        }
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
    public DeliveryPersonModel verifyDeliveryPerson(int id) {
        Optional<DeliveryPersonModel> deliveryPerson = deliveryPersonRepo.findById(id);
        if(deliveryPerson.isPresent()){
            DeliveryPersonModel verifyDeliveryPerson = deliveryPerson.get();
            verifyDeliveryPerson.setVerificationStatus(VerificationStatus.VERIFIED);
            return deliveryPersonRepo.save(verifyDeliveryPerson);
        }else{
            throw new IllegalArgumentException("No store found with this ID");
        }
    }
    public DeliveryPersonModel revokeDeliveryPerson(int id) {
        Optional<DeliveryPersonModel> deliveryPerson = deliveryPersonRepo.findById(id);
        if(deliveryPerson.isPresent()){
            DeliveryPersonModel revokeDeliveryPerson = deliveryPerson.get();
            revokeDeliveryPerson.setVerificationStatus(VerificationStatus.NOT_VERIFIED);
            return deliveryPersonRepo.save(revokeDeliveryPerson);
        }else{
            throw new IllegalArgumentException("No store found with this ID");
        }
    }
    public List<User> getAllUsers() {
        return userRepo.findAll();
    }
    public List<MedicalStoreModel> getAllMedicalStores() {
        return medicalStoreRepo.findAll();
    }
    public List<DeliveryPersonModel> getAllDeliveryPersons() {
        return deliveryPersonRepo.findAll();
    }
    public void removeDeliveryPerson(int id) {
        if (deliveryPersonRepo.existsById(id)) {
            deliveryPersonRepo.deleteById(id);
        } else {
            throw new IllegalArgumentException("No delivery person found with this ID");
        }
    }
    public void removeStore(int id) {
        if(medicalStoreRepo.existsById(id)){
            medicalStoreRepo.deleteById(id);
        }else  {
            throw new IllegalArgumentException("No Medical store found with this ID");
        }
    }
}
