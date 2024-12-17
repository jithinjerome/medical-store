package com.example.medical.store.Prescription;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PrescriptionRepository extends JpaRepository<Prescription, Long>{
    List<Prescription> findByUserId(long userId);
}
