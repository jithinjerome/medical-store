package com.example.medical.store.DeliveryPerson;

import com.example.medical.store.User.Role;
import com.example.medical.store.User.VerificationStatus;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

@Entity
public class DeliveryPersonModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "delivery_person_id")
    private int deliveryPersonId;

    @Column(name = "name")
    @NotBlank(message = "Name is required")
    private String name;

    @Column(name = "email", nullable = false, unique = true)
    @Email(message = "Invalid email address")
    @NotBlank(message = "Email is required")
    private String email;

    @Column(name = "phone_number")
    @Pattern(regexp = "\\d{10}", message = "Phone number must be 10 digits")
    private String contactNo;

    @Column(name = "driving_license_image_url")
    private String DrivingLicenseImageUrl;

    @Column(name = "driving_license_image_name")
    private String DrivingLicenseImageName;

    @Column(name = "driving_license_image_size")
    private Long DrivingLicenseImageSize;

    @Column(name = "driving_license_image_type")
    private String DrivingLicenseImageType;

    @Column(name = "password")
    @NotBlank(message = "Password is required")
    @Size(min = 8, message = "Password must be at least 8 characters long")
    private String password;

    @Enumerated(EnumType.STRING)
    @Column(name = "verification_status")
    private VerificationStatus verificationStatus;

    @Enumerated(EnumType.STRING)
    private Role role;

    // Getters and setters

    public int getDeliveryPersonId() {
        return deliveryPersonId;
    }

    public void setDeliveryPersonId(int deliveryPersonId) {
        this.deliveryPersonId = deliveryPersonId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getContactNo() {
        return contactNo;
    }

    public void setContactNo(String contactNo) {
        this.contactNo = contactNo;
    }

    public String getDrivingLicenseImageUrl() {
        return DrivingLicenseImageUrl;
    }

    public void setDrivingLicenseImageUrl(String drivingLicenseImageUrl) {
        DrivingLicenseImageUrl = drivingLicenseImageUrl;
    }

    public String getDrivingLicenseImageName() {
        return DrivingLicenseImageName;
    }

    public void setDrivingLicenseImageName(String drivingLicenseImageName) {
        DrivingLicenseImageName = drivingLicenseImageName;
    }

    public Long getDrivingLicenseImageSize() {
        return DrivingLicenseImageSize;
    }

    public void setDrivingLicenseImageSize(Long drivingLicenseImageSize) {
        DrivingLicenseImageSize = drivingLicenseImageSize;
    }

    public String getDrivingLicenseImageType() {
        return DrivingLicenseImageType;
    }

    public void setDrivingLicenseImageType(String drivingLicenseImageType) {
        DrivingLicenseImageType = drivingLicenseImageType;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public VerificationStatus getVerificationStatus() {
        return verificationStatus;
    }

    public void setVerificationStatus(VerificationStatus verificationStatus) {
        this.verificationStatus = verificationStatus;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }
}
