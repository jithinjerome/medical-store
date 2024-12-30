package com.example.medical.store.MedicalStore;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
public class MedicalStoreModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int storeId;

    @Column(name = "medical_store_name", nullable = false)
    @NotBlank(message = "Store name is required")
    private String storeName;
    @Column(name = "storeownername", nullable = false)
    @NotBlank(message = "Store Owner name is required")
    private String storeOwnerName;


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
    public @NotBlank(message = "Store Owner name is required") String getStoreOwnerName() {
        return storeOwnerName;
    }

    public void setStoreOwnerName(@NotBlank(message = "Store Owner name is required") String storeOwnerName) {
        this.storeOwnerName = storeOwnerName;
    }

    public @NotBlank(message = "Store address is required") String getStoreAddress() {
        return storeAddress;
    }

    public void setStoreAddress(@NotBlank(message = "Store address is required") String storeAddress) {
        this.storeAddress = storeAddress;
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

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }
}
