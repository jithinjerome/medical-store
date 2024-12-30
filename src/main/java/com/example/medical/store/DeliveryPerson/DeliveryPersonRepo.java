package com.example.medical.store.DeliveryPerson;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface DeliveryPersonRepo extends JpaRepository<DeliveryPersonModel,Integer> {
     Optional<DeliveryPersonModel> findByEmail(String email);
}
