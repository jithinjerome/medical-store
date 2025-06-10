package com.example.medical.store.Expense;

import com.example.medical.store.MedicalStore.MedicalStoreModel;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Expense {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "medical_store_id", nullable = false)
    private MedicalStoreModel medicalStore;  // Linking expenses to a store

    private LocalDate expenseDate;
    private double amount;
    private String category;
}

