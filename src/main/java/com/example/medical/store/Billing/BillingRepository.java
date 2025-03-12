package com.example.medical.store.Billing;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BillingRepository extends JpaRepository<Billing, Long> {
    List findByUserId(long userId);
}
