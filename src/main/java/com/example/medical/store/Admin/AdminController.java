package com.example.medical.store.Admin;


import com.example.medical.store.DeliveryPerson.DeliveryPersonModel;
import com.example.medical.store.MedicalStore.MedicalStoreModel;
import com.example.medical.store.MedicalStore.MedicalStoreService;
import com.example.medical.store.User.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("/api/auth/admin")
public class AdminController {

    @Autowired
    private AdminService adminService;

    @Autowired
    private MedicalStoreService medicalStoreService;




    @PostMapping("/login")
    public ResponseEntity<String> adminLogin(@RequestBody AdminModel loginRequest) {
        try {
            String loginResponse = adminService.adminLogin(loginRequest.getEmail(), loginRequest.getPassword());
            return new ResponseEntity<>(loginResponse, HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.UNAUTHORIZED);
        }
    }


    @GetMapping("/users")
    public ResponseEntity<List<User>> getAllUsers() {
        try {
            List<User> users = adminService.getAllUsers();
            return new ResponseEntity<>(users, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/medical-stores")
    public ResponseEntity<List<MedicalStoreModel>> getAllMedicalStores() {
        try {
            List<MedicalStoreModel> medicalstores = adminService.getAllMedicalStores();
            return new ResponseEntity<>(medicalstores, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    @GetMapping("/delivery-persons")
    public ResponseEntity<List<DeliveryPersonModel>> getAllDeliveryPersons() {
        try {
            List<DeliveryPersonModel> deliverypersons = adminService.getAllDeliveryPersons();
            return new ResponseEntity<List<DeliveryPersonModel>>(deliverypersons, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    @PutMapping(path = "/verifyMedicalStore/{storeId}")
    public ResponseEntity<?> verifyMedicalStore(@PathVariable int storeId){
        try{
            MedicalStoreModel verifiedStore = adminService.verifiedStore(storeId);
            return new ResponseEntity<>(verifiedStore, HttpStatus.OK);
        }catch (Exception e){
            return new ResponseEntity<>(e.getMessage(),HttpStatus.NOT_FOUND);
        }
    }

    @PutMapping(path = "/revokeVerifyStore/{storeId}")
    public ResponseEntity<?> revokeVerifyStore(@PathVariable int storeId){
        try{
            MedicalStoreModel verifiedStore = adminService.revokeVerifiedStore(storeId);
            return new ResponseEntity<>(verifiedStore, HttpStatus.OK);
        }catch (Exception e){
            return new ResponseEntity<>(e.getMessage(),HttpStatus.NOT_FOUND);
        }
    }

    @PutMapping(path = "/verifyDeliveryPerson/{personId}")
    public ResponseEntity<?> verifyDeliveryPerson(@PathVariable int personId){
        try{
            DeliveryPersonModel verifyDeliveryPerson = adminService.verifyDeliveryPerson(personId);
            return new ResponseEntity<>(verifyDeliveryPerson, HttpStatus.OK);
        }catch (Exception e){
            return new ResponseEntity<>(e.getMessage(),HttpStatus.NOT_FOUND);
        }
    }

    @PutMapping(path = "/revokeDeliveryPerson/{personId}")
    public ResponseEntity<?> revokeDeliveryPerson(@PathVariable int personId){
        try{
            DeliveryPersonModel revokeDeliveryPerson = adminService.revokeDeliveryPerson(personId);
            return new ResponseEntity<>(revokeDeliveryPerson, HttpStatus.OK);
        }catch (Exception e){
            return new ResponseEntity<>(e.getMessage(),HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping(path = "/removeDeliveryPerson/{personId}")
    public ResponseEntity<?> removeDeliveryPerson(@PathVariable int personId) {
        try {
            adminService.removeDeliveryPerson(personId);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT); // No response body
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping(path = "/removeStore/{storeId}")
    public ResponseEntity<?> removeStore(@PathVariable int storeId) {
        try {
            adminService.removeStore(storeId);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT); // No response body
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }
}
