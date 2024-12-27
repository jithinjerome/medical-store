package com.example.medical.store.DeliveryPerson;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
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
    @Column(name = "delivery_person_id")
    private int deliveryPersonId;

    @Column(name = "name", nullable = false)
    @NotBlank(message = "Name is required")
    private String name;

    @Column(name = "email", nullable = false, unique = true)
    @Email(message = "Invalid email address")
    @NotBlank(message = "Email is required")
    private String email;

    @Column(name = "password", nullable = false)
    @NotBlank(message = "Password is required")
    @Size(min = 8, message = "Password must be at least 8 characters long")
    private String password;

    @Enumerated(EnumType.STRING)
    @Column(name = "verification_status", nullable = false)
    private VerificationStatus verificationStatus;

    // Enum for Verification Status
    public enum VerificationStatus {
        VERIFIED,
        NOT_VERIFIED
    }
}
