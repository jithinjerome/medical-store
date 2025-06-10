package com.example.medical.store.MedicalStore;

import com.example.medical.store.AWS.FileUploadService;
import com.example.medical.store.Exceptions.MedicalStoreException;
import com.example.medical.store.JWT.AuthResponse;
import com.example.medical.store.JWT.JWTUtil;
import com.example.medical.store.Prescription.PrescriptionRequest;
import com.example.medical.store.Prescription.PrescriptionRequestRepository;
import com.example.medical.store.StoreEmployee.StoreEmployeeRepository;
import com.example.medical.store.User.Role;
import com.example.medical.store.User.VerificationStatus;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
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

@Slf4j
@Service
public class MedicalStoreService {

    @Autowired
    private  MedicalStoreRepo medicalStoreRepo;
    @Autowired
    private  PasswordEncoder passwordEncoder;
    @Autowired
    private  JWTUtil jwtUtil;
    @Autowired
    private  FileUploadService fileUploadService;
    @Autowired
    private  StoreEmployeeRepository storeEmployeeRepository;
    @Autowired
    private  PrescriptionRequestRepository prescriptionRequestRepository;

    @Transactional
    public MedicalStoreDTO registerMedicalStore(MedicalStoreDTO medicalStoreDTO, @Valid MultipartFile licenseImage) throws IOException {
        Optional<MedicalStoreModel> existingMedicalStore = medicalStoreRepo.findByEmail(medicalStoreDTO.getEmail());
        if (existingMedicalStore.isPresent()) {
            throw new MedicalStoreException("Medical store with this email already exists.", HttpStatus.CONFLICT);
        }
        MedicalStoreModel medicalStoreModel = convertToModel(medicalStoreDTO);

        // Encode password
        if (medicalStoreDTO.getPassword() != null && !medicalStoreDTO.getPassword().isEmpty()) {
            medicalStoreModel.setPassword(passwordEncoder.encode(medicalStoreDTO.getPassword()));
        } else {
            throw new MedicalStoreException("Password cannot be null or empty", HttpStatus.BAD_REQUEST);
        }
        System.out.println("Received Password: " + medicalStoreDTO.getPassword());


        // Handle file upload
        if (licenseImage != null && !licenseImage.isEmpty()) {
            validateAndUploadFile(medicalStoreModel, licenseImage);
        }

        // Save the medical store entity
         MedicalStoreModel savedMedicalStore = medicalStoreRepo.save(medicalStoreModel);

       return convertToDTO(savedMedicalStore);
    }

    public ResponseEntity<?> medicalStoreLogin(String email, String password) {
        Optional<MedicalStoreModel> medicalStoreModelOptional = medicalStoreRepo.findByEmail(email);

        if(medicalStoreModelOptional.isPresent()){
            MedicalStoreModel medicalStore = medicalStoreModelOptional.get();
            if(passwordEncoder.matches(password,medicalStore.getPassword())){
                String accessToken = jwtUtil.generateToken(medicalStore.getStoreId(),medicalStore.getEmail(), medicalStore.getRole().name());
                String refreshToken = jwtUtil.generateRefreshToken(medicalStore.getEmail());
                return ResponseEntity.ok(new AuthResponse(accessToken,refreshToken));
            }else{
                return ResponseEntity.badRequest().body("Invalid credentials: Password mismatch");
            }
        }
        String jwtToken = jwtUtil.generateToken(medicalStore.getStoreId(), email, medicalStore.getRole().toString());
        System.out.println("Generated JWT Token: " + jwtToken);
        return ResponseEntity.badRequest().body("Invalid credentials: User not found");
    }

        return jwtToken;

    }

//    public ResponseEntity<List<PrescriptionRequest>> allPrescriptions(int storeId) {
//        Optional<MedicalStoreModel> medicalStoreModelOptional = medicalStoreRepo.findById(storeId);
//        if(medicalStoreModelOptional.isPresent()){
//            List<PrescriptionRequest> prescriptionRequestList = prescriptionRequestRepository.findByStoreId(storeId);
//            return new ResponseEntity<>(prescriptionRequestList,HttpStatus.OK);
//        }
//        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
//
//    }

    public MedicalStoreDTO convertToDTO(MedicalStoreModel model) {
        MedicalStoreDTO dto = new MedicalStoreDTO();
        dto.setStoreId(model.getStoreId());
        dto.setStoreName(model.getStoreName());
        dto.setStoreOwnerName(model.getStoreOwnerName());
        dto.setStoreAddress(model.getStoreAddress());
        dto.setLicenseNo(model.getLicenseNo());
        dto.setContactNo(model.getContactNo());
        dto.setEmail(model.getEmail());
        dto.setStoreLicenseImageUrl(model.getStoreLicenseImageUrl()); // Standardized field name
        dto.setVerificationStatus(model.getVerificationStatus());
        dto.setLatitude(model.getLatitude());
        dto.setLongitude(model.getLongitude());
        dto.setRole(model.getRole());
        return dto;
    }

    public MedicalStoreModel convertToModel(MedicalStoreDTO dto) {
        MedicalStoreModel model = new MedicalStoreModel();
        model.setStoreName(dto.getStoreName());
        model.setStoreOwnerName(dto.getStoreOwnerName());
        model.setStoreAddress(dto.getStoreAddress());
        model.setLicenseNo(dto.getLicenseNo());
        model.setContactNo(dto.getContactNo());
        model.setEmail(dto.getEmail());
        // Ensure password is securely handled
        if (dto.getPassword() != null) {
            model.setPassword(passwordEncoder.encode(dto.getPassword()));
        }
        model.setStoreLicenseImageUrl(dto.getStoreLicenseImageUrl());
        model.setVerificationStatus(VerificationStatus.NOT_VERIFIED);
        model.setLatitude(dto.getLatitude());
        model.setLongitude(dto.getLongitude());
        model.setRole(Role.MEDICALSTORE);
        return model;
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
                throw new MedicalStoreException("Failed to upload store license image. Please try again.", HttpStatus.INTERNAL_SERVER_ERROR);
            }


            model.setStoreLicenseImageUrl(fileUrl);
            model.setStoreLicenseImageName(originalFilename);
            model.setStoreLicenseImageSize(file.getSize());
            model.setStoreLicenseImageType(file.getContentType());
        } catch (Exception e) {
            throw new MedicalStoreException("An unexpected error occurred while uploading the file. Please try again.", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public ResponseEntity<List<MedicalStoreModel>> verifiedStores() {
        List<MedicalStoreModel> stores = medicalStoreRepo.findByVerificationStatus(VerificationStatus.VERIFIED);
        return new ResponseEntity<>(stores,HttpStatus.OK);
    }
}
