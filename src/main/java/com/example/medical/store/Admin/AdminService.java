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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

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

    @Value("${spring.mail.username}")
    private String fromEmailId;

    private static final Logger logger = LoggerFactory.getLogger(AdminService.class);

    public void sendEmail(String email, String name, String role, boolean isVerified) {
        try {
            String subject = role + (isVerified ? " Verification Completed" : " Verification Revoked");
            String messageBody = "Dear " + name + ",\n\n" +
                    (isVerified ? "Congratulations! Your account has been verified and approved.\n" :
                            "We regret to inform you that your verification has been revoked. Please contact support for further details.\n") +
                    "\nThank you for using our service.\n\nBest Regards,\nE-Pharma";

            MimeMessage mimeMessage = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, false);
            helper.setFrom("E-Pharma Support <" + fromEmailId + ">");
            helper.setTo(email);
            helper.setSubject(subject);
            helper.setText(messageBody);

            javaMailSender.send(mimeMessage);
            logger.info("Email sent successfully to {}", email);
        } catch (MailException | MessagingException e) {
            logger.error("Failed to send email to {}: {}", email, e.getMessage());
        }
    }

    public String adminLogin(String email, String password) {
        Optional<AdminModel> adminOptional = adminRepo.findByEmail(email);
        if (adminOptional.isPresent()) {
            AdminModel admin = adminOptional.get();
            if (passwordEncoder.matches(password, admin.getPassword())) {
                return jwtUtil.generateToken(admin.getAdminId(), admin.getEmail(), admin.getRole().name());
            } else {
                throw new IllegalArgumentException("Invalid Credentials: Password mismatch");
            }
        }
        throw new IllegalArgumentException("Invalid Credentials: User not found");
    }

    public MedicalStoreModel verifyStore(int id) {
        return medicalStoreRepo.findById(id).map(store -> {
            store.setVerificationStatus(VerificationStatus.VERIFIED);
            sendEmail(store.getEmail(), store.getStoreName(), "Medical Store", true);
            return medicalStoreRepo.save(store);
        }).orElseThrow(() -> new IllegalArgumentException("No store found with this ID"));
    }

    public MedicalStoreModel revokeStoreVerification(int id) {
        return medicalStoreRepo.findById(id).map(store -> {
            store.setVerificationStatus(VerificationStatus.NOT_VERIFIED);
            sendEmail(store.getEmail(), store.getStoreName(), "Medical Store", false);
            return medicalStoreRepo.save(store);
        }).orElseThrow(() -> new IllegalArgumentException("No store found with this ID"));
    }

    public DeliveryPersonModel verifyDeliveryPerson(int id) {
        return deliveryPersonRepo.findById(id).map(person -> {
            person.setVerificationStatus(VerificationStatus.VERIFIED);
            sendEmail(person.getEmail(), person.getName(), "Delivery Person", true);
            return deliveryPersonRepo.save(person);
        }).orElseThrow(() -> new IllegalArgumentException("No delivery person found with this ID"));
    }

    public DeliveryPersonModel revokeDeliveryPerson(int id) {
        return deliveryPersonRepo.findById(id).map(person -> {
            person.setVerificationStatus(VerificationStatus.NOT_VERIFIED);
            sendEmail(person.getEmail(), person.getName(), "Delivery Person", false);
            return deliveryPersonRepo.save(person);
        }).orElseThrow(() -> new IllegalArgumentException("No delivery person found with this ID"));
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
        if (medicalStoreRepo.existsById(id)) {
            medicalStoreRepo.deleteById(id);
        } else {
            throw new IllegalArgumentException("No medical store found with this ID");
        }
    }
}
