package com.example.medical.store.MedicalStore;

import com.example.medical.store.User.VerificationStatus;
import jakarta.validation.constraints.Email;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MedicalStoreRepo extends JpaRepository<MedicalStoreModel,Integer>{
    Optional<MedicalStoreModel> findByEmail(@Email(message = "Invalid email address") String email);

    List<MedicalStoreModel> findByVerificationStatus(VerificationStatus verificationStatus);
}
