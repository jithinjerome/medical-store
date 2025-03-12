package com.example.medical.store.MedicalStore;

import com.example.medical.store.User.Role;
import com.example.medical.store.User.VerificationStatus;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class MedicalStoreModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int storeId;

    @Column(name = "medical_store_name")
    @NotBlank(message = "Store name is required")
    private String storeName;

    @Column(name = "storeownername")
    @NotBlank(message = "Store Owner name is required")
    private String storeOwnerName;

    @Column(name = "address")
    @NotBlank(message = "Store address is required")
    private String storeAddress;

    @Column(name = "license_no")
    private String licenseNo;

    @Column(name = "phone_number")
    @Pattern(regexp = "\\d{10}", message = "Phone number must be 10 digits")
    private String contactNo;

    @Column(name = "email", unique = true)
    @Email(message = "Invalid email address")
    private String email;

    @Column(name = "password")
    @NotBlank(message = "Password is required")
    @Size(min = 8, message = "Password must be at least 8 characters long")
    private String password;

    @Enumerated(EnumType.STRING)
    private VerificationStatus verificationStatus = VerificationStatus.NOT_VERIFIED;

    @Column(name = "latitude")
    private Double latitude;

    @Column(name = "longitude")
    private Double longitude;

    @Column(name = "license_image_url")
    private String licenseImageUrl;

    @Column(name = "license_image_name")
    private String licenseImageName;

    @Column(name = "license_image_size")
    private Long licenseImageSize;

    @Column(name = "license_image_type")
    private String licenseImageType;

    // Getter and Setter


    public String getLicenseImageUrl() {
        return licenseImageUrl;
    }

    public void setLicenseImageUrl(String licenseImageUrl) {
        this.licenseImageUrl = licenseImageUrl;
    }

    public String getLicenseImageName() {
        return licenseImageName;
    }

    public void setLicenseImageName(String licenseImageName) {
        this.licenseImageName = licenseImageName;
    }

    public Long getLicenseImageSize() {
        return licenseImageSize;
    }

    public void setLicenseImageSize(Long licenseImageSize) {
        this.licenseImageSize = licenseImageSize;
    }

    public String getLicenseImageType() {
        return licenseImageType;
    }

    public void setLicenseImageType(String licenseImageType) {
        this.licenseImageType = licenseImageType;
    }

    @Enumerated(EnumType.STRING)
    private Role role;

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }


    public VerificationStatus getVerificationStatus() {
        return verificationStatus;
    }

    public void setVerificationStatus(VerificationStatus verificationStatus) {
        this.verificationStatus = verificationStatus;
    }

    public int getStoreId() {
        return storeId;
    }

    public void setStoreId(int storeId) {
        this.storeId = storeId;
    }

    public String getStoreName() {
        return storeName;
    }

    public void setStoreName(String storeName) {
        this.storeName = storeName;
    }

    public String getStoreOwnerName() {
        return storeOwnerName;
    }

    public void setStoreOwnerName(String storeOwnerName) {
        this.storeOwnerName = storeOwnerName;
    }

    public String getStoreAddress() {
        return storeAddress;
    }

    public void setStoreAddress(String storeAddress) {
        this.storeAddress = storeAddress;
    }

    public String getLicenseNo() {
        return licenseNo;
    }

    public void setLicenseNo(String licenseNo) {
        this.licenseNo = licenseNo;
    }

    public String getContactNo() {
        return contactNo;
    }

    public void setContactNo(@Pattern(regexp = "\\d{10}", message = "Phone number must be 10 digits") String contactNo) {
        this.contactNo = contactNo;
    }

    public @Email(message = "Invalid email address") String getEmail() {
        return email;
    }

    public void setEmail(@Email(message = "Invalid email address") String email) {
        this.email = email;
    }

    public @NotBlank(message = "Password is required") @Size(min = 8, message = "Password must be at least 8 characters long") String getPassword() {
        return password;
    }

    public void setPassword(@NotBlank(message = "Password is required") @Size(min = 8, message = "Password must be at least 8 characters long") String password) {
        this.password = password;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }
}
