package com.example.medical.store.Admin;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AdminRepo extends JpaRepository<AdminModel,String> {
    Optional<AdminModel> findByEmail(String email);
}
