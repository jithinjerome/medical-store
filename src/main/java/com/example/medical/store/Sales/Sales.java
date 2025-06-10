package com.example.medical.store.Sales;

import com.example.medical.store.MedicalStore.MedicalStoreModel;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Sales {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "medical_store_id", nullable = false)
    private MedicalStoreModel medicalStore;  // Linking sales to a store

    private LocalDate saleDate;
    private double totalSales;

}
