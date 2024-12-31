package com.example.medical.store.MedicalStore;

import com.example.medical.store.Admin.AdminModel;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = "/api/auth/medicalstore")
public class MedicalStoreController {

    @PostMapping("/register")
    public ResponseEntity<String> medicalStoreRegister(@Valid @RequestBody MedicalStoreModel medicalStoreModel) {
        return ResponseEntity.ok("Medical store registered successfully");
    }

    @PostMapping("/login")
    public ResponseEntity<String> medicalStoreLogin(@Valid @RequestParam String email, @RequestParam String password) {
        return ResponseEntity.ok("Medical store logged in successfully");
    }
}
