package com.example.medical.store.EmployeeWage;

import com.example.medical.store.MedicalStore.MedicalStoreModel;
import com.example.medical.store.StoreEmployee.StoreEmployee;
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
public class EmployeeWage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "employee_id", nullable = false)
    private StoreEmployee employee;  // Link wage to a specific employee

    @ManyToOne
    @JoinColumn(name = "medical_store_id", nullable = false)
    private MedicalStoreModel medicalStore;  // Link wage to a specific store

    private double salary;
    private LocalDate paymentDate;
}

