package com.example.medical.store.DeliveryPerson;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class DeliveryPersonModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "Delivery Person's ID")
    private int deliveryPersonId;
    @Column(name = "Name of Delivery Person",nullable = false)
    private String name;
    @Column(name = "Email",nullable = false,unique = true)
    private String email;
    @Column(name = "Password",nullable = false)
    private String password;
    @Column(name = "Verification Status",nullable = false)
    private String verificationStatus;
}
