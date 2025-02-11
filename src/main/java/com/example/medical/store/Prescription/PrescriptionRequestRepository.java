package com.example.medical.store.Prescription;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PrescriptionRequestRepository extends JpaRepository<PrescriptionRequest, Long> {
    Optional<PrescriptionRequest> findByPrescriptionId(long prescriptionId);
    boolean existsByPrescriptionIdAndStoreId(long prescriptionId, int storeId);

    Optional<PrescriptionRequest> findByPrescriptionIdAndStoreId(long prescriptionId, long storeId);

    List<PrescriptionRequest> findByStoreId(int storeId);
}
