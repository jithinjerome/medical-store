package com.example.medical.store.MedicalStore;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class MedicalStoreModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int storeId;

    @Column(name = "medical_store_name", nullable = false)
    @NotBlank(message = "Store name is required")
    private String storeName;

    @Column(name = "address", nullable = false)
    @NotBlank(message = "Store address is required")
    private String storeAddress;

    @Column(name = "phone_number", nullable = false)
    @Pattern(regexp = "\\d{10}", message = "Phone number must be 10 digits")
    private String contactNo;

    @Column(name = "email", nullable = false, unique = true)
    @Email(message = "Invalid email address")
    private String email;

    @Column(name = "password", nullable = false)
    @NotBlank(message = "Password is required")
    @Size(min = 8, message = "Password must be at least 8 characters long")
    private String password;

    @Column(name = "latitude")
    private String latitude;

    @Column(name = "longitude")
    private String longitude;
}
