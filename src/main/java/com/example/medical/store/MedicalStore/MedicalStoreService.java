package com.example.medical.store.MedicalStore;

import com.example.medical.store.AWS.FileUploadService;
import com.example.medical.store.Exceptions.MedicalStoreException;
import com.example.medical.store.JWT.JWTUtil;
import com.example.medical.store.Prescription.PrescriptionRequestRepository;
import com.example.medical.store.StoreEmployee.StoreEmployeeRepository;
import com.example.medical.store.User.Role;
import com.example.medical.store.User.VerificationStatus;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Optional;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class MedicalStoreService {

    private final MedicalStoreRepo medicalStoreRepo;
    private final PasswordEncoder passwordEncoder;
    private final JWTUtil jwtUtil;
    private final FileUploadService fileUploadService;
    private final StoreEmployeeRepository storeEmployeeRepository;
    private final PrescriptionRequestRepository prescriptionRequestRepository;

    @Transactional
    public MedicalStoreDTO registerMedicalStore(MedicalStoreDTO medicalStoreDTO, @Valid MultipartFile licenseImage) throws IOException {
        Optional<MedicalStoreModel> existingMedicalStore = medicalStoreRepo.findByEmail(medicalStoreDTO.getEmail());
        if (existingMedicalStore.isPresent()) {
            throw new MedicalStoreException("Medical store with this email already exists.", HttpStatus.CONFLICT);
        }
        MedicalStoreModel medicalStoreModel = convertToModel(medicalStoreDTO);

        // Encode password
        medicalStoreModel.setPassword(passwordEncoder.encode(medicalStoreDTO.getPassword()));

        // Handle file upload
        if (licenseImage != null && !licenseImage.isEmpty()) {
            validateAndUploadFile(medicalStoreModel, licenseImage);
        }

        // Save the medical store entity
         MedicalStoreModel savedMedicalStore = medicalStoreRepo.save(medicalStoreModel);
        log.info("Medical store registered successfully: ID {}", savedMedicalStore.getStoreId());

       return convertToDTO(savedMedicalStore);
    }

    public String medicalStoreLogin(String email, String password) {
        MedicalStoreModel medicalStore = medicalStoreRepo.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("Invalid email.Please try again"));
        if (!passwordEncoder.matches(password, medicalStore.getPassword())) {
            throw new IllegalArgumentException("Invalid password.Please try again");
        }
        return "Login successful!";
    }
    public MedicalStoreDTO convertToDTO(MedicalStoreModel model) {
        return MedicalStoreDTO.builder()
                .storeId(model.getStoreId())
                .storeName(model.getStoreName())
                .storeOwnerName(model.getStoreOwnerName())
                .storeAddress(model.getStoreAddress())
                .licenseNo(model.getLicenseNo())
                .contactNo(model.getContactNo())
                .email(model.getEmail())
                .licenseImageUrl(model.getLicenseImageUrl()) // Standardized field name
                .verificationStatus(model.getVerificationStatus())
                .latitude(model.getLatitude())
                .longitude(model.getLongitude())
                .role(model.getRole())
                .build();
    }
    public MedicalStoreModel convertToModel(MedicalStoreDTO dto) {
        return MedicalStoreModel.builder()
                .storeName(dto.getStoreName())
                .storeOwnerName(dto.getStoreOwnerName())
                .storeAddress(dto.getStoreAddress())
                .licenseNo(dto.getLicenseNo())
                .contactNo(dto.getContactNo())
                .email(dto.getEmail())
                .verificationStatus(VerificationStatus.NOT_VERIFIED)
                .role(Role.MEDICALSTORE)
                .build();
    }
    private void validateAndUploadFile(MedicalStoreModel model, MultipartFile file) throws IOException {

        if (file.getContentType() == null || !file.getContentType().startsWith("image/")) {
            throw new MedicalStoreException("Only image files are allowed.", HttpStatus.BAD_REQUEST);
        }

        long MAX_SIZE = 10 * 1024 * 1024; // 10MB
        if (file.getSize() > MAX_SIZE) {
            throw new MedicalStoreException("File size exceeds the maximum allowed size (10MB).", HttpStatus.BAD_REQUEST);
        }

        String originalFilename = file.getOriginalFilename();
        if (originalFilename == null || originalFilename.isEmpty()) {
            throw new MedicalStoreException("Invalid file name. Please upload a valid image.", HttpStatus.BAD_REQUEST);
        }
        String fileKey = "medical-store/licenses/" + UUID.randomUUID() + "_" + originalFilename;
        try {
            String fileUrl = fileUploadService.uploadFile("medicalstore", fileKey, file.getBytes());

            if (fileUrl == null || fileUrl.isEmpty()) {
                log.error("File upload failed for file: {}", originalFilename);
                throw new MedicalStoreException("Failed to upload store license image. Please try again.", HttpStatus.INTERNAL_SERVER_ERROR);
            }


            model.setLicenseImageUrl(fileUrl);
            model.setLicenseImageName(originalFilename);
            model.setLicenseImageSize(file.getSize());
            model.setLicenseImageType(file.getContentType());
        } catch (Exception e) {
            log.error("Unexpected error occurred during file upload: {}", e.getMessage(), e);
            throw new MedicalStoreException("An unexpected error occurred while uploading the file. Please try again.", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
