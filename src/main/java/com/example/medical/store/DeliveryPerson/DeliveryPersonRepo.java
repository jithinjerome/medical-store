package com.example.medical.store.DeliveryPerson;


import com.example.medical.store.User.VerificationStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DeliveryPersonRepo extends JpaRepository<DeliveryPersonModel,Integer> {
     Optional<DeliveryPersonModel> findByEmail(String email);

    List<DeliveryPersonModel> findByVerificationStatus(VerificationStatus verificationStatus);
}
