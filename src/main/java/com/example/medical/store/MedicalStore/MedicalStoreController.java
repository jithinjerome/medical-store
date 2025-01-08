package com.example.medical.store.MedicalStore;

import com.example.medical.store.Admin.AdminModel;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "/api/auth/medicalstore")
public class MedicalStoreController {

    @Autowired
    private MedicalStoreService medicalStoreService;

    @PostMapping("/register")
    public ResponseEntity<?> medicalStoreRegister(@Valid @RequestBody MedicalStoreModel medicalStoreModel) {
        try{
            MedicalStoreModel registeredMedicalStore = medicalStoreService.registerMedicalStore(medicalStoreModel);
            return new ResponseEntity<>(registeredMedicalStore, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(),HttpStatus.BAD_REQUEST);
        }

    }

    @PostMapping("/login")
    public ResponseEntity<String> medicalStoreLogin(@RequestBody MedicalStoreModel medicalStoreModel) {
        try {
            String loginResponse = medicalStoreService.medicalStoreLogin(medicalStoreModel.getEmail(), medicalStoreModel.getPassword());
            return new ResponseEntity<>(loginResponse, HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.UNAUTHORIZED);
        }
    }

    @GetMapping(path = "/allStores")
    public ResponseEntity<List<MedicalStoreModel>> allStores(){
        return medicalStoreService.allStores();
    }

    @GetMapping(path = "/verifiedStores")
    public ResponseEntity<List<MedicalStoreModel>> verifiedStores(){
        return medicalStoreService.verifiedStores();
    }

    @GetMapping(path = "/notVerified")
    public ResponseEntity<List<MedicalStoreModel>> notVerifiedStores(){
        return medicalStoreService.notVerifiedStores();
    }
}
