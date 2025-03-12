package com.example.medical.store.MedicalStore;

import com.example.medical.store.User.Role;
import com.example.medical.store.User.VerificationStatus;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;

@Entity
@Getter
@Setter
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
    private VerificationStatus verificationStatus;

    @Column(name = "latitude")
    private Double latitude;

    @Column(name = "longitude")
    private Double longitude;

    @Enumerated(EnumType.STRING)
    private Role role;

    @Column(name = "store_license_image_url")
    private String StoreLicenseImageUrl;

    @Column(name = "store_license_image_name")
    private String StoreLicenseImageName;

    @Column(name = "store_license_image_size")
    private Long StoreLicenseImageSize;

    @Column(name = "store_license_image_type")
    private String StoreLicenseImageType;

}