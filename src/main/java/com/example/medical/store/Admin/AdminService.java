package com.example.medical.store.Admin;
import com.example.medical.store.DeliveryPerson.DeliveryPersonModel;
import com.example.medical.store.DeliveryPerson.DeliveryPersonRepo;
import com.example.medical.store.JWT.JWTUtil;
import com.example.medical.store.MedicalStore.MedicalStoreModel;
import com.example.medical.store.MedicalStore.MedicalStoreRepo;
import com.example.medical.store.User.User;
import com.example.medical.store.User.UserRepository;
import com.example.medical.store.User.VerificationStatus;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
    private UserRepository userRepo;

    @Autowired
    private MedicalStoreRepo medicalStoreRepo;

    @Autowired
    private DeliveryPersonRepo deliveryPersonRepo;

    @Autowired
    private JavaMailSender javaMailSender;

    @Autowired
    private TemplateEngine templateEngine;

    private static final Logger logger = LoggerFactory.getLogger(AdminService.class);


    public void sendVerificationEmail(String email, String storeName, String role) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(email);
            message.setSubject(role + " Verification Completed");
            message.setText("Dear " + storeName + ",\n" +
                    "Congratulations! Your account has been verified and approved.\n" +
                    "You can now access all platform features.\n" +
                    "\n" +
                    "Thank you for using our service.\n" +
                    "\n" +
                    "Best Regards,\n" +
                    "E-pharma");
            javaMailSender.send(message);
            logger.info("Verification email sent successfully to {}", email);
        }catch (MailException e){
            logger.error("Failed to sent verification email to {}",email + e.getMessage());
        }
    }

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

    public MedicalStoreModel verifiedStore(int id) {
        Optional<MedicalStoreModel> medicalStore = medicalStoreRepo.findById(id);
        if(medicalStore.isPresent()){
            MedicalStoreModel verifiedStore = medicalStore.get();
            verifiedStore.setVerificationStatus(VerificationStatus.VERIFIED);
            sendVerificationEmail(verifiedStore.getEmail(), verifiedStore.getStoreName(), "Medical Store");
            medicalStoreRepo.save(verifiedStore);
            return verifiedStore;
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

    public DeliveryPersonModel verifyDeliveryPerson(int id) {
        Optional<DeliveryPersonModel> deliveryPerson = deliveryPersonRepo.findById(id);
        if(deliveryPerson.isPresent()){
            DeliveryPersonModel verifyDeliveryPerson = deliveryPerson.get();
            verifyDeliveryPerson.setVerificationStatus(VerificationStatus.VERIFIED);

            // Send verification email
            sendVerificationEmail(verifyDeliveryPerson.getEmail(), verifyDeliveryPerson.getName(), "Delivery Partner");
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
