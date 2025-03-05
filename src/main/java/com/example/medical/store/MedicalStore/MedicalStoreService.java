package com.example.medical.store.MedicalStore;

import com.example.medical.store.AWS.FileUploadService;
import com.example.medical.store.JWT.JWTUtil;
import com.example.medical.store.Prescription.PrescriptionRequest;
import com.example.medical.store.Prescription.PrescriptionRequestRepository;
import com.example.medical.store.User.Role;
import com.example.medical.store.User.VerificationStatus;
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
public class MedicalStoreService {

    @Autowired
    private MedicalStoreRepo medicalStoreRepo;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JWTUtil jwtUtil;

    @Autowired
    private PrescriptionRequestRepository prescriptionRequestRepository;
    @Autowired
    public FileUploadService fileUploadService;


    public MedicalStoreModel registerMedicalStore(MedicalStoreModel medicalStoreModel, MultipartFile storeLicenseImage) throws IOException {
        // Check if the email is already registered
        if (medicalStoreRepo.findByEmail(medicalStoreModel.getEmail()).isPresent()) {
            throw new IllegalArgumentException("Medical store with this email already exists.");
        }

        // Set default verification status and role using Optional
        medicalStoreModel.setVerificationStatus(
                Optional.ofNullable(medicalStoreModel.getVerificationStatus()).orElse(VerificationStatus.NOT_VERIFIED)
        );
        medicalStoreModel.setRole(
                Optional.ofNullable(medicalStoreModel.getRole()).orElse(Role.MEDICALSTORE)
        );

        // Encode password
        medicalStoreModel.setPassword(passwordEncoder.encode(medicalStoreModel.getPassword()));

        // Validate and upload store license image (if provided)
        if (storeLicenseImage != null && !storeLicenseImage.isEmpty()) {
            // Validate the file
            if (!storeLicenseImage.getContentType().startsWith("image/")) {
                throw new IllegalArgumentException("Only image files are allowed.");
            }

            long MAX_SIZE = 10 * 1024 * 1024; // 10MB
            if (storeLicenseImage.getSize() > MAX_SIZE) {
                throw new IllegalArgumentException("File size exceeds the maximum allowed size (10MB).");
            }

            String originalFilename = storeLicenseImage.getOriginalFilename();
            if (originalFilename == null || originalFilename.isEmpty()) {
                throw new IllegalArgumentException("Invalid file name. Please upload a valid image.");
            }

            // Upload file
            String fileKey = "medical-store/licenses/" + UUID.randomUUID() + "_" + originalFilename;
            String fileUrl = fileUploadService.uploadFile("medicalstore", fileKey, storeLicenseImage.getBytes());

            if (fileUrl == null || fileUrl.isEmpty()) {
                throw new IllegalArgumentException("Failed to upload store license image. Please try again.");
            }

            // Set model properties after successful upload
            medicalStoreModel.setStoreLicenseImageUrl(fileUrl);
            medicalStoreModel.setStoreLicenseImageName(originalFilename);
            medicalStoreModel.setStoreLicenseImageSize(storeLicenseImage.getSize());
            medicalStoreModel.setStoreLicenseImageType(storeLicenseImage.getContentType());
        }

        // Save and return the medical store entity
        return medicalStoreRepo.save(medicalStoreModel);
    }
    public String medicalStoreLogin( String email, String password) {
        Optional<MedicalStoreModel> medicalStoreOptional = medicalStoreRepo.findByEmail(email);
        if(medicalStoreOptional.isPresent()){
            MedicalStoreModel medicalStore = medicalStoreOptional.get();
            if(passwordEncoder.matches(password, medicalStore.getPassword())){
                return jwtUtil.generateToken(medicalStore.getStoreId(),medicalStore.getEmail(), medicalStore.getRole().name());
            }
            throw new IllegalArgumentException("Invalid Credentials: Password Missmatch");
        }
        throw new IllegalArgumentException("Invalid Credentials: User not found");
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