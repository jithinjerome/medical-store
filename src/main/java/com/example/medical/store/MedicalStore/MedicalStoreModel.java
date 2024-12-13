package com.example.medical.store.MedicalStore;

import jakarta.persistence.*;
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
    @Column(name = "Medical Store Name",nullable = false)
    private String storeName;
    @Column(name = "Address",nullable = false)
    private String storeAddress;
    @Column(name = "Phone Number",nullable = false)
    private int contactNo;
    @Column(name = "Email",nullable = false,unique = true)
    private String email;
    @Column(name = "Password",nullable = false)
    private String password;
}
