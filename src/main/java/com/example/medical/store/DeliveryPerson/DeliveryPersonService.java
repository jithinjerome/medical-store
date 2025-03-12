package com.example.medical.store.DeliveryPerson;

import com.example.medical.store.AWS.FileUploadService;
import com.example.medical.store.JWT.JWTUtil;
import com.example.medical.store.User.Role;
import com.example.medical.store.User.VerificationStatus;
import jakarta.transaction.Transactional;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class DeliveryPersonService {

    @Autowired
    private DeliveryPersonRepo deliveryPersonRepo;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JWTUtil jwtUtil;

    @Autowired
    private FileUploadService fileUploadService;

    @Transactional
    public DeliveryPersonModel registerDeliveryPerson(DeliveryPersonModel deliveryPersonModel, MultipartFile drivingLicenseImage) throws IOException {
        Optional<DeliveryPersonModel> existingDeliveryPerson = deliveryPersonRepo.findByEmail(deliveryPersonModel.getEmail());
        if (existingDeliveryPerson.isPresent()) {
            throw new IllegalArgumentException("Delivery person with this email already exists");
        }

        if (deliveryPersonModel.getVerificationStatus() == null) {
            deliveryPersonModel.setVerificationStatus(VerificationStatus.NOT_VERIFIED);
        }

        if (deliveryPersonModel.getRole() == null) {
            deliveryPersonModel.setRole(Role.DELIVERYPERSON);
        }

        deliveryPersonModel.setPassword(passwordEncoder.encode(deliveryPersonModel.getPassword()));

        // Validate file before upload
        if (!drivingLicenseImage.getContentType().startsWith("image/")) {
            throw new IllegalArgumentException("Only image files are allowed");
        }

        long MAX_SIZE = 10 * 1024 * 1024; // 10MB
        if (drivingLicenseImage.getSize() > MAX_SIZE) {
            throw new IllegalArgumentException("File size exceeds the maximum allowed size (10MB)");
        }

        String originalFilename = drivingLicenseImage.getOriginalFilename();
        if (originalFilename == null || originalFilename.isEmpty()) {
            throw new IllegalArgumentException("Invalid file name. Please upload a valid image.");
        }

        // Upload file after validation
        String fileKey = "delivery-person/licenses/" + UUID.randomUUID() + "_" + originalFilename;
        String fileUrl = fileUploadService.uploadFile("medicalstore", fileKey, drivingLicenseImage.getBytes());

        if (fileUrl == null || fileUrl.isEmpty()) {
            throw new IllegalArgumentException("Failed to upload license image. Please try again.");
        }

        // Set model properties after validation
        deliveryPersonModel.setDrivingLicenseImageUrl(fileUrl);
        deliveryPersonModel.setDrivingLicenseImageName(originalFilename);
        deliveryPersonModel.setDrivingLicenseImageSize(drivingLicenseImage.getSize());
        deliveryPersonModel.setDrivingLicenseImageType(drivingLicenseImage.getContentType());

        return deliveryPersonRepo.save(deliveryPersonModel);
    }

    public String deliveryPersonLogin(@Email(message = "Invalid email address") @NotBlank(message = "Email is required") String email,
                                      @NotBlank(message = "Password is required") @Size(min = 8, message = "Password must be at least 8 characters long") String password) {
        Optional<DeliveryPersonModel> deliveryPersonOptional = deliveryPersonRepo.findByEmail(email);

        if (deliveryPersonOptional.isPresent()) {
            DeliveryPersonModel deliveryPerson = deliveryPersonOptional.get();
            if (passwordEncoder.matches(password, deliveryPerson.getPassword())) {
                return jwtUtil.generateToken(deliveryPerson.getDeliveryPersonId(), deliveryPerson.getEmail(), deliveryPerson.getRole().name());
            } else {
                throw new IllegalArgumentException("Invalid credentials: Password mismatch");
            }
        }
        throw new IllegalArgumentException("Invalid credentials: User not found");
    }

    public ResponseEntity<List<DeliveryPersonModel>> allDeliveryPersons() {
        List<DeliveryPersonModel> deliveryPersons = deliveryPersonRepo.findAll();
        return new ResponseEntity<>(deliveryPersons, HttpStatus.OK);
    }

    public ResponseEntity<List<DeliveryPersonModel>> verifiedPersons() {
        List<DeliveryPersonModel> verifiedPersons = deliveryPersonRepo.findByVerificationStatus(VerificationStatus.VERIFIED);

        if (verifiedPersons.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(verifiedPersons, HttpStatus.OK);
    }

    public ResponseEntity<List<DeliveryPersonModel>> notVerifiedPerson() {
        List<DeliveryPersonModel> notVerified = deliveryPersonRepo.findByVerificationStatus(VerificationStatus.NOT_VERIFIED);

        if (notVerified.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(notVerified, HttpStatus.OK);
    }
}
