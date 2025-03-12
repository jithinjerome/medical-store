package com.example.medical.store.MedicalStore;

import com.example.medical.store.User.Role;
import com.example.medical.store.User.VerificationStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MedicalStoreDTO {
    private int storeId;
    private String storeName;
    private String storeOwnerName;
    private String storeAddress;
    private String licenseNo;
    private String contactNo;
    private String email;
    private VerificationStatus verificationStatus;
    private Double latitude;
    private Double longitude;
    private String licenseImageUrl;
    private Role role;
}
