package com.example.medical.store.MedicalStore;

import com.example.medical.store.User.Role;
import com.example.medical.store.User.VerificationStatus;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
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

    private String licenseImageUrl;
    private String licenseImageName;
    private Long licenseImageSize;
    private String licenseImageType;
}
