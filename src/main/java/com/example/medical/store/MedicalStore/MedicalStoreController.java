package com.example.medical.store.MedicalStore;

import com.example.medical.store.Admin.AdminModel;
import com.example.medical.store.Prescription.PrescriptionRequest;

import com.example.medical.store.DeliveryPerson.DeliveryPersonModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping(path = "/api/auth/medical-store")
public class MedicalStoreController {

    @Autowired
    private MedicalStoreService medicalStoreService;

@PostMapping("/register")
public ResponseEntity<?> medicalStoreRegister(@RequestPart("medicalStoreModel") MedicalStoreModel medicalStoreModel,
                                                @RequestPart("storeLicenseImage") MultipartFile storeLicenseImage) {
    try {
        MedicalStoreModel registeredMedicalStore = medicalStoreService.registerMedicalStore(medicalStoreModel,storeLicenseImage);
        return new ResponseEntity<>(registeredMedicalStore, HttpStatus.CREATED);
    } catch (IllegalArgumentException | IOException e) {
        return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
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

    @GetMapping(path = "/allPrescriptions/{storeId}")
    public ResponseEntity<List<PrescriptionRequest>> allRequests(@PathVariable int storeId){
        return medicalStoreService.allPrescriptions(storeId);
    }
}
