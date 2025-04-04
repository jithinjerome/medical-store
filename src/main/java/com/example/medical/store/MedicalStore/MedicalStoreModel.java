package com.example.medical.store.MedicalStore;

import com.example.medical.store.User.Role;
import com.example.medical.store.User.VerificationStatus;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MedicalStoreModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int storeId;

    @Column(nullable = false)
    @NotBlank(message = "Store name is required")
    private String storeName;

    @Column(nullable = false)
    @NotBlank(message = "Store owner name is required")
    private String storeOwnerName;

    @Column(nullable = false)
    @NotBlank(message = "Store address is required")
    private String storeAddress;

    private String licenseNo;

    @Pattern(regexp = "\\d{10}", message = "Phone number must be 10 digits")
    private String contactNo;

    @Column(nullable = false, unique = true)
    @Email(message = "Invalid email address")
    private String email;

    @Column(nullable = false)
    @NotBlank(message = "Password is required")
    @Size(min = 8, message = "Password must be at least 8 characters long")
    private String password;

    @Enumerated(EnumType.STRING)
    private VerificationStatus verificationStatus;

    private Double latitude;
    private Double longitude;

    @Enumerated(EnumType.STRING)
    private Role role;

    private String storeLicenseImageUrl;
    private String storeLicenseImageName;
    private Long storeLicenseImageSize;
    private String storeLicenseImageType;

    public int getStoreId() {
        return storeId;
    }

    public void setStoreId(int storeId) {
        this.storeId = storeId;
    }

    public @NotBlank(message = "Store name is required") String getStoreName() {
        return storeName;
    }

    public void setStoreName(@NotBlank(message = "Store name is required") String storeName) {
        this.storeName = storeName;
    }

    public @NotBlank(message = "Store owner name is required") String getStoreOwnerName() {
        return storeOwnerName;
    }

    public void setStoreOwnerName(@NotBlank(message = "Store owner name is required") String storeOwnerName) {
        this.storeOwnerName = storeOwnerName;
    }

    public @NotBlank(message = "Store address is required") String getStoreAddress() {
        return storeAddress;
    }

    public void setStoreAddress(@NotBlank(message = "Store address is required") String storeAddress) {
        this.storeAddress = storeAddress;
    }

    public String getLicenseNo() {
        return licenseNo;
    }

    public void setLicenseNo(String licenseNo) {
        this.licenseNo = licenseNo;
    }

    public @Pattern(regexp = "\\d{10}", message = "Phone number must be 10 digits") String getContactNo() {
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

    public VerificationStatus getVerificationStatus() {
        return verificationStatus;
    }

    public void setVerificationStatus(VerificationStatus verificationStatus) {
        this.verificationStatus = verificationStatus;
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

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public String getStoreLicenseImageUrl() {
        return storeLicenseImageUrl;
    }

    public void setStoreLicenseImageUrl(String storeLicenseImageUrl) {
        this.storeLicenseImageUrl = storeLicenseImageUrl;
    }

    public String getStoreLicenseImageName() {
        return storeLicenseImageName;
    }

    public void setStoreLicenseImageName(String storeLicenseImageName) {
        this.storeLicenseImageName = storeLicenseImageName;
    }

    public Long getStoreLicenseImageSize() {
        return storeLicenseImageSize;
    }

    public void setStoreLicenseImageSize(Long storeLicenseImageSize) {
        this.storeLicenseImageSize = storeLicenseImageSize;
    }

    public String getStoreLicenseImageType() {
        return storeLicenseImageType;
    }

    public void setStoreLicenseImageType(String storeLicenseImageType) {
        this.storeLicenseImageType = storeLicenseImageType;
    }
}
