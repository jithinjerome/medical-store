package com.example.medical.store.Admin;


import com.example.medical.store.DeliveryPerson.DeliveryPersonModel;
import com.example.medical.store.MedicalStore.MedicalStoreModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequestMapping("/api/auth/admin")
public class AdminController {

    @Autowired
    private AdminService adminService;


    @PostMapping("/login")
    public ResponseEntity<String> adminLogin(@RequestBody AdminModel loginRequest) {
        try {
            String loginResponse = adminService.adminLogin(loginRequest.getEmail(), loginRequest.getPassword());
            return new ResponseEntity<>(loginResponse, HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.UNAUTHORIZED);
        }
    }

    @PutMapping(path = "/verify/{id}")
    public ResponseEntity<?> verifyPerson(@PathVariable int id){
        try{
            DeliveryPersonModel verifiedPerson = adminService.verifiedPerson(id);
            return new ResponseEntity<>(verifiedPerson,HttpStatus.OK);
        }catch(Exception e){
            return new ResponseEntity<>(e.getMessage(),HttpStatus.NOT_FOUND);
        }

    }

    @PutMapping(path = "/verifyStore/{id}")
    public ResponseEntity<?> verifyStore(@PathVariable int id){
        try{
            MedicalStoreModel verifiedStore = adminService.verifiedStore(id);
            return new ResponseEntity<>(verifiedStore, HttpStatus.OK);
        }catch (Exception e){
            return new ResponseEntity<>(e.getMessage(),HttpStatus.NOT_FOUND);
        }
    }
}
