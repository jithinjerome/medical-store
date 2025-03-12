@Transactional
public MedicalStoreDTO registerMedicalStore(@Valid MedicalStoreDTO medicalStoreDTO, @Valid MultipartFile licenseImage) throws IOException {
    Optional<MedicalStoreModel> existingMedicalStore = medicalStoreRepo.findByEmail(medicalStoreDTO.getEmail());
    if (existingMedicalStore.isPresent()) {
        throw new IllegalArgumentException("Medical store with this email already exists.");
    }

    // Convert DTO to Model
    MedicalStoreModel medicalStoreModel = new MedicalStoreModel();
    medicalStoreModel.setStoreName(medicalStoreDTO.getStoreName());
    medicalStoreModel.setStoreOwnerName(medicalStoreDTO.getStoreOwnerName());
    medicalStoreModel.setStoreAddress(medicalStoreDTO.getStoreAddress());
    medicalStoreModel.setLicenseNo(medicalStoreDTO.getLicenseNo());
    medicalStoreModel.setContactNo(medicalStoreDTO.getContactNo());
    medicalStoreModel.setEmail(medicalStoreDTO.getEmail());

    // Set default values
    medicalStoreModel.setVerificationStatus(VerificationStatus.NOT_VERIFIED);
    medicalStoreModel.setRole(Role.MEDICALSTORE);

    // Encode password
    medicalStoreModel.setPassword(passwordEncoder.encode(medicalStoreDTO.getPassword()));

    // Validate and upload license image
    if (licenseImage != null && !licenseImage.isEmpty()) {
        if (!licenseImage.getContentType().startsWith("image/")) {
            throw new IllegalArgumentException("Only image files are allowed.");
        }

        long MAX_SIZE = 10 * 1024 * 1024; // 10MB
        if (licenseImage.getSize() > MAX_SIZE) {
            throw new IllegalArgumentException("File size exceeds the maximum allowed size (10MB).");
        }

        String originalFilename = licenseImage.getOriginalFilename();
        if (originalFilename == null || originalFilename.isEmpty()) {
            throw new IllegalArgumentException("Invalid file name. Please upload a valid image.");
        }

        // Upload file
        String fileKey = "medical-store/licenses/" + UUID.randomUUID() + "_" + originalFilename;
        String fileUrl = fileUploadService.uploadFile("medicalstore", fileKey, licenseImage.getBytes());

        if (fileUrl == null || fileUrl.isEmpty()) {
            throw new IllegalArgumentException("Failed to upload store license image. Please try again.");
        }

        // Set image properties
        medicalStoreModel.setStoreLicenseImageUrl(fileUrl);
        medicalStoreModel.setStoreLicenseImageName(originalFilename);
        medicalStoreModel.setStoreLicenseImageSize(licenseImage.getSize());
        medicalStoreModel.setStoreLicenseImageType(licenseImage.getContentType());
    }

    // Save the medical store entity
    MedicalStoreModel savedMedicalStore = medicalStoreRepo.save(medicalStoreModel);

    // Convert Model to DTO and return
    return convertToDTO(savedMedicalStore);
}
