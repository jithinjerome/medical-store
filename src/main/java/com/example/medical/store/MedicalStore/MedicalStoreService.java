package com.example.medical.store.MedicalStore;

import com.example.medical.store.AWS.FileUploadService;
import com.example.medical.store.JWT.JWTUtil;
import com.example.medical.store.StoreEmployee.StoreEmployeeRepository;
import com.example.medical.store.User.Role;
import com.example.medical.store.User.VerificationStatus;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
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
    private FileUploadService fileUploadService;

    @Autowired
    private StoreEmployeeRepository storeEmployeeRepository;


    @Transactional
    public MedicalStoreModel registerMedicalStore(@Valid MedicalStoreModel medicalStoreModel,@Valid MultipartFile licenseImage) throws IOException {
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
        String fileKey = "medical-store/licenses/" + UUID.randomUUID() + "_" + licenseImage.getOriginalFilename();
        String fileUrl = fileUploadService.uploadFile("medicalstore", fileKey, licenseImage.getBytes());
        medicalStoreModel.setLicenseImageUrl(fileUrl);
        medicalStoreModel.setLicenseImageName(licenseImage.getOriginalFilename());
        medicalStoreModel.setLicenseImageSize(licenseImage.getSize());
        medicalStoreModel.setLicenseImageType(licenseImage.getContentType());
        if (medicalStoreModel.getLicenseImageUrl() == null || medicalStoreModel.getLicenseImageUrl().isEmpty()) {
            throw new IllegalArgumentException("License image URL must be provided");
        }
        if (medicalStoreModel.getLicenseImageName() == null || medicalStoreModel.getLicenseImageName().isEmpty()) {
            throw new IllegalArgumentException("License image name must be provided");
        }
        if (medicalStoreModel.getLicenseImageSize() <= 0) {
            throw new IllegalArgumentException("License image size must be greater than zero");
        }
        if (medicalStoreModel.getLicenseImageType() == null || medicalStoreModel.getLicenseImageType().isEmpty()) {
            throw new IllegalArgumentException("License image type must be provided");
        }
        if (!licenseImage.getContentType().startsWith("image/")) {
            throw new IllegalArgumentException("Only image files are allowed");
        }

        long MAX_SIZE = 10 * 1024 * 1024;
        if (licenseImage.getSize() > MAX_SIZE) {
            throw new IllegalArgumentException("File size exceeds the maximum allowed size");
        }



        return medicalStoreRepo.save(medicalStoreModel);

    }

    public String medicalStoreLogin( String email, String password) {
        Optional<MedicalStoreModel> medicalStoreOptional = medicalStoreRepo.findByEmail(email);
        if(medicalStoreOptional.isPresent()){
            MedicalStoreModel medicalStore = medicalStoreOptional.get();

            // TEMPORARY: Compare plain text passwords for testing
            if (password.equals(medicalStore.getPassword())) {
                return jwtUtil.generateToken(medicalStore.getEmail(), medicalStore.getRole().name());
            }

            if(passwordEncoder.matches(password, medicalStore.getPassword())){
                return jwtUtil.generateToken(medicalStore.getEmail(), medicalStore.getRole().name());
            }
            throw new IllegalArgumentException("Invalid Credentials: Password Mismatch");
        }
        throw new IllegalArgumentException("Invalid Credentials: User not found");
    }

    public MedicalStoreDTO convertToDTO(MedicalStoreModel medicalStoreModel) {
        return new MedicalStoreDTO(
                medicalStoreModel.getStoreId(),
                medicalStoreModel.getStoreName(),
                medicalStoreModel.getStoreOwnerName(),
                medicalStoreModel.getStoreAddress(),
                medicalStoreModel.getLicenseNo(),
                medicalStoreModel.getContactNo(),
                medicalStoreModel.getEmail(),
                medicalStoreModel.getVerificationStatus(),
                medicalStoreModel.getLatitude(),
                medicalStoreModel.getLongitude(),
                medicalStoreModel.getLicenseImageUrl(),
                medicalStoreModel.getRole()
        );
    }



}
