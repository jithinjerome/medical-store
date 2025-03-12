package com.example.medical.store.Admin;

import com.example.medical.store.User.Role;
import jakarta.persistence.*;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

@Setter
@Getter
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class AdminModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "admin_id")
    private int adminId;

    @Column(name = "email", nullable = false, unique = true)
    @Email(message = "Invalid email address")
    @NotBlank(message = "Email is required")
    private String email;

    @Column(name = "password", nullable = false)
    @NotBlank(message = "Password is required")
    @Size(min = 8, message = "Password must be at least 8 characters long")
    private String password;

    @Enumerated(EnumType.STRING)
    private Role role;

}
